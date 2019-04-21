package org.graph;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class TasksGraph {
    private Graph<Task, DefaultEdge> graph;
    private AllDirectedPaths<Task, DefaultEdge> paths;

    TasksGraph(String fileName) {
        this.graph = new DirectedMultigraph<>(DefaultEdge.class);
        createGraph(fileName);

        this.paths = new AllDirectedPaths<>(this.graph);
    }

    Graph<Task, DefaultEdge> getGraph() {
        return graph;
    }

    public AllDirectedPaths<Task, DefaultEdge> getPaths() {
        return paths;
    }

    private void createGraph(String fileName) {
        String line = null;

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
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
