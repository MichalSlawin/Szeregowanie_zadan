package org.graph;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.alg.shortestpath.*;

import java.util.List;

import static org.graph.GraphTests.*;

public class Main {
    public static void main(String[] args) {
        createGraphTest();
    }
}

/*
 Graph<org.graph.Task, DefaultEdge> g = new DirectedMultigraph<>(DefaultEdge.class);

        org.graph.Task task1 = new org.graph.Task("task1", 2);
        org.graph.Task task2 = new org.graph.Task("task2", 3);
        org.graph.Task task3 = new org.graph.Task("task3", 4);
        org.graph.Task task4 = new org.graph.Task("task4", 2);

        g.addVertex(task1);
        g.addVertex(task2);
        g.addVertex(task3);
        g.addVertex(task4);
        task4.setEnd(true);
        task1.setStart(true);

        g.addEdge(task1, task2);
        g.addEdge(task2, task4);
        g.addEdge(task1, task3);
        g.addEdge(task3, task4);

        AllDirectedPaths<org.graph.Task, DefaultEdge> paths = new AllDirectedPaths<>(g);
        List<GraphPath<org.graph.Task, DefaultEdge>> pathsList = paths.getAllPaths(task1, task4, true, null);

        for(GraphPath<org.graph.Task, DefaultEdge> graphPath : pathsList) {
            System.out.println("Path: ");
            List<org.graph.Task> tasks = graphPath.getVertexList();
            int duration = 0;
            for(org.graph.Task task : tasks) {
                System.out.println(task);
                duration += task.getDuration();
            }
            System.out.println(duration);
        }

        System.out.println(g.outgoingEdgesOf(task1));

*/
