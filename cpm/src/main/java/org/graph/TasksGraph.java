package org.graph;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class TasksGraph {
    private Graph<Task, DefaultEdge> graph;
    private List<Path> pathsList;
    private List<Machine> timeTable;
    private Path criticalPath;
    private int cMax = -1;
    private int maxEarliestFinish = -1;

    TasksGraph(String fileName) {
        this.graph = new DirectedMultigraph<>(DefaultEdge.class);
        createGraph(fileName);
        setStartAndEndTasks();
    }

    public int getcMax() {
        return cMax;
    }

    public int getMaxEarliestFinish() {
        return maxEarliestFinish;
    }

    Graph<Task, DefaultEdge> getGraph() {
        return graph;
    }

    List<Path> getPathsList() {
        return pathsList;
    }

    Path getCriticalPath() {
        return criticalPath;
    }

    public void setTimeTable(List<Machine> timeTable) {
        this.timeTable = timeTable;
    }

    public void cpm() {
        setStartsAndFinishes();
        setTimeTableCPM();
    }

    private void setStartsAndFinishes() {
        List<Task> startTasks = new ArrayList<>();
        List<Task> endTasks = new ArrayList<>();
        Set<Task> allTasks = graph.vertexSet();
        for(Task task : allTasks) {
            if(task.isStart()) {
                task.setEarliestFinish(task.getDuration());
                startTasks.add(task);
            }
        }
        setEarliestStartsAndFinishes(startTasks);

        for(Task task : allTasks) {
            if(task.isEnd()) {
                task.setLatestFinish(this.maxEarliestFinish);
                endTasks.add(task);
            }
        }
        setLatestStartsAndFinishes(endTasks);

        setCriticalTasks();
    }

    private void setLatestStartsAndFinishes(List<Task> tasks) {
        List<Task> tasksRec = new ArrayList<>();
        for(Task task : tasks) {
            if(task.getLatestFinish() > this.cMax) {
                this.cMax = task.getLatestFinish();
            }

            Set<DefaultEdge> edges = this.graph.incomingEdgesOf(task);
            for(DefaultEdge edge : edges) {
                Task source = this.graph.getEdgeSource(edge);

                if (source.getLatestFinish() == -1 || (source.getLatestFinish() != -1
                        && source.getLatestFinish() > task.getLatestStart())) {

                    source.setLatestFinish(task.getLatestStart());
                }
                tasksRec.add(source);
            }
            setLatestStartsAndFinishes(tasksRec);
        }
    }
    private void setEarliestStartsAndFinishes(List<Task> tasks) {
        List<Task> tasksRec = new ArrayList<>();
        for(Task task : tasks) {
            if(task.getEarliestFinish() > this.maxEarliestFinish) {
                this.maxEarliestFinish = task.getEarliestFinish();
            }

//            System.out.println(task);
            Set<DefaultEdge> edges = this.graph.outgoingEdgesOf(task);
            for(DefaultEdge edge : edges) {
                Task target = this.graph.getEdgeTarget(edge);

                if(target.getEarliestStart() == -1 || (target.getEarliestStart() != -1
                        && target.getEarliestStart() < task.getEarliestFinish())) {

                    target.setEarliestStart(task.getEarliestFinish());
                }
                tasksRec.add(target);
            }
            setEarliestStartsAndFinishes(tasksRec);
        }
    }

    public List<Machine> getTimeTable() {
        return timeTable;
    }

    public StringBuilder getTimeTableString() {
        StringBuilder sb = new StringBuilder();
        for(Machine machine : timeTable) {
            sb.append(machine + "\n");
        }
        return sb;
    }

    private void setTimeTableCPM() {
        this.timeTable = new ArrayList<>();
        int machineNumber = 1;
        boolean taskAdded;

        Machine criticalMachine = new Machine(this.cMax, machineNumber);
        machineNumber++;
        criticalMachine.setTasks(getCriticalTasks());
        this.timeTable.add(criticalMachine);

        Set<Task> allTasks = this.graph.vertexSet();

        for(Task task : allTasks) {
            taskAdded = false;
            if(!task.isCritical()) {
                for(Machine machine : this.timeTable) {
                    if(!taskAdded && !machine.isOccupied(task.getEarliestStart(), task.getLatestFinish())) {
                        machine.addTaskCPM(task);
                        taskAdded = true;
                    }
                }
                if(!taskAdded) {
                    Machine machine = new Machine(this.cMax, machineNumber);
                    machineNumber++;
                    machine.addTaskCPM(task);
                    this.timeTable.add(machine);
                }
            }
        }
    }

    private List<GraphPath<Task, DefaultEdge>> getAllPaths(Task startTask, Task endTask) {
        AllDirectedPaths<Task, DefaultEdge> paths = new AllDirectedPaths<>(this.graph);
        return paths.getAllPaths(startTask, endTask, true, null);
    }

    private void createPaths() {
        List<Task> startTasksList = getStartTasks();
        List<Task> endTasksList = getEndTasks();
        int criticalPathDuration = 0;
        Path criticalPath = null;

//        System.out.println("Paths:");
        for(Task startTask : startTasksList) {
            for(Task endTask : endTasksList) {
                List<GraphPath<Task, DefaultEdge>> allPaths = getAllPaths(startTask, endTask);
                for(GraphPath<Task, DefaultEdge> path : allPaths) {
//                    System.out.println(path);
                    int duration = 0;
                    for(Task task : path.getVertexList()) {
                        duration += task.getDuration();
                    }
                    Path pathToAdd = new Path(startTask, endTask, path, duration);
                    this.pathsList.add(pathToAdd);
                    if(duration > criticalPathDuration) {
                        criticalPathDuration = duration;
                        criticalPath = pathToAdd;
                    }
                }
            }
        }
        this.criticalPath = criticalPath;
        if (this.criticalPath != null) {
            this.cMax = this.criticalPath.getDuration();
        } else {
            System.out.println("Critical path is empty!");
        }

    }

    private void createGraph(String fileName) {
        String line;

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                createTasksAndEdge(line);
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }

    private void createTasksAndEdge(String line) {
        int taskDuration;
        int dashIndex = line.indexOf('-');
        if(dashIndex != -1) {
//            System.out.println(line);
            String taskStr = line.substring(0, dashIndex);
            String taskName = taskStr.substring(0, taskStr.indexOf('('));

            Task task1 = findTaskByName(taskName);
            if(task1 == null) {
                taskDuration = Integer.parseInt(taskStr.substring(taskStr.indexOf('(')+1, taskStr.indexOf(')')));
                task1 = new Task(taskName, taskDuration);
                this.graph.addVertex(task1);
            }

            taskStr = line.substring(dashIndex+1);
            taskName = taskStr.substring(0, taskStr.indexOf('('));
//            System.out.println(taskStr);
            Task task2 = findTaskByName(taskName);
            if(task2 == null) {
                taskDuration = Integer.parseInt(taskStr.substring(taskStr.indexOf('(')+1, taskStr.indexOf(')')));
                task2 = new Task(taskName, taskDuration);
                this.graph.addVertex(task2);
            }
            if(this.graph.getEdge(task2, task1) == null) {
                this.graph.addEdge(task1, task2);
            }
        } else {
            String taskName = line.substring(0, line.indexOf('('));
            Task task = findTaskByName(taskName);
            if(task == null) {
                taskDuration = Integer.parseInt(line.substring(line.indexOf('(')+1, line.indexOf(')')));
                task = new Task(taskName, taskDuration);
                this.graph.addVertex(task);
            }
        }
    }

    private void setStartAndEndTasks() {
        for(Task task : this.graph.vertexSet()) {
            if(graph.incomingEdgesOf(task).isEmpty()) {
                task.setStart(true);
            }
            if(graph.outgoingEdgesOf(task).isEmpty()) {
                task.setEnd(true);
            }
        }
    }

    private void setCriticalTasks() {
        Task startTask = null;
        for(Task task : this.graph.vertexSet()) {
            if(task.getEarliestStart() == task.getLatestStart() && task.getEarliestFinish() == task.getLatestFinish()) {
                task.setCritical(true);
            }
        }
        for(Task task : this.graph.vertexSet()) {
            if(task.isCritical() && task.isStart()) {
                startTask = task;
            }
        }
        setCriticalTasksPath(startTask);
    }

    private void setCriticalTasksPath(Task task) {
        List<Task> criticalPath = new ArrayList<>();
        boolean taskAdded;

        criticalPath.add(task);

        while(!task.isEnd()) {
            taskAdded = false;
            Set<DefaultEdge> edges = this.graph.edgesOf(task);
            for(DefaultEdge edge : edges) {
                Task targetTask = graph.getEdgeTarget(edge);
                if(targetTask.isCritical() && targetTask != task && !taskAdded) {
                    criticalPath.add(targetTask);
                    task = targetTask;
                    taskAdded = true;
                }
            }
        }
        for(Task taskFromAll : this.graph.vertexSet()) {
            if(!criticalPath.contains(taskFromAll)) {
                taskFromAll.setCritical(false);
            }
        }
    }

    private List<Task> getStartTasks() {
        List<Task> tasksList = new ArrayList<>();
        Set<Task> tasksSet = this.graph.vertexSet();
        for(Task task : tasksSet) {
            if(task.isStart()) {
                tasksList.add(task);
            }
        }
        return tasksList;
    }

    private List<Task> getEndTasks() {
        List<Task> tasksList = new ArrayList<>();
        Set<Task> tasksSet = this.graph.vertexSet();
        for(Task task : tasksSet) {
            if(task.isEnd()) {
                tasksList.add(task);
            }
        }
        return tasksList;
    }

    List<Task> getCriticalTasks() {
        List<Task> tasks = new ArrayList<>();
        for(Task task : this.graph.vertexSet()) {
            if(task.isCritical()) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    private Task findTaskByName(String name) {
        Set<Task> tasksSet = this.graph.vertexSet();
        for(Task task : tasksSet) {
            if(task.getName().equals(name)) {
                return task;
            }
        }
        return null;
    }
}
