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
    private Path criticalPath;
    private int cMax;

    TasksGraph(String fileName) {
        this.graph = new DirectedMultigraph<>(DefaultEdge.class);
        this.pathsList = new ArrayList<>();
        createGraph(fileName);
        setStartAndEndTasks();
        createPaths();
        setCriticalStartsAndFinishes();
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

    private void setCriticalStartsAndFinishes() {
        GraphPath<Task, DefaultEdge> path = this.criticalPath.getPath();
        List<Task> tasks = path.getVertexList();
        Task lastTask = tasks.get(tasks.size()-1);

        lastTask.setEarliestFinish(this.cMax);
        lastTask.setLatestFinish(this.cMax);
        lastTask.setCritical(true);

        for(int i = tasks.size()-2; i >= 0; i--) {
            Task task = tasks.get(i);
            task.setEarliestFinish(tasks.get(i+1).getEarliestStart());
            task.setLatestFinish(tasks.get(i+1).getLatestStart());
            task.setCritical(true);
        }

        List<Task> startTasks = new ArrayList<>();
        Set<Task> allTasks = graph.vertexSet();
        for(Task task : allTasks) {
            if(task.isEnd()) {
                task.setLatestFinish(this.cMax);
                tasks.add(task);
            }
            if(task.isStart()) {
                task.setEarliestFinish(task.getDuration());
                startTasks.add(task);
            }
        }
        setLatestStartsAndFinishes(tasks);
        setEarliestStartsAndFinishes(startTasks);
    }

    private void setLatestStartsAndFinishes(List<Task> tasks) {
        List<Task> tasksRec = new ArrayList<>();
        for(Task task : tasks) {
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
        System.out.println("W earliest");
        for(Task task : tasks) {
            System.out.println(task);
            Set<DefaultEdge> edges = this.graph.outgoingEdgesOf(task);
            for(DefaultEdge edge : edges) {
                Task target = this.graph.getEdgeTarget(edge);
                System.out.println("target: " + target);
                if(target.getEarliestStart() == -1 || (target.getEarliestStart() != -1
                        && target.getEarliestStart() < task.getEarliestFinish())) {

                    target.setEarliestStart(task.getEarliestFinish());
                }
                tasksRec.add(target);
            }
            setEarliestStartsAndFinishes(tasksRec);
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
            graph.addEdge(task1, task2);
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
