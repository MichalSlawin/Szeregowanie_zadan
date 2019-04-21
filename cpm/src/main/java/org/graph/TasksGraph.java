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
import java.util.Iterator;
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

        for(int i = tasks.size()-2; i >= 0; i--) {
            Task task = tasks.get(i);
            task.setEarliestFinish(tasks.get(i+1).getEarliestStart());
            task.setLatestFinish(tasks.get(i+1).getLatestStart());
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

        for(Task startTask : startTasksList) {
            for(Task endTask : endTasksList) {
                List<GraphPath<Task, DefaultEdge>> allPaths = getAllPaths(startTask, endTask);
                for(GraphPath<Task, DefaultEdge> path : allPaths) {
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
        String taskStr = line.substring(0, dashIndex);
        String taskName = taskStr.substring(0, line.indexOf('('));

        Task task1 = findTaskByName(taskName);
        if(task1 == null) {
            taskDuration = Integer.parseInt(taskStr.substring(taskStr.indexOf('(')+1, taskStr.indexOf(')')));
            task1 = new Task(taskName, taskDuration);
            this.graph.addVertex(task1);
        }
        taskStr = line.substring(dashIndex+1);
        taskName = taskStr.substring(0, line.indexOf('('));

        Task task2 = findTaskByName(taskName);
        if(task2 == null) {
            taskDuration = Integer.parseInt(taskStr.substring(taskStr.indexOf('(')+1, taskStr.indexOf(')')));
            task2 = new Task(taskName, taskDuration);
            this.graph.addVertex(task2);
        }
        graph.addEdge(task1, task2);
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
