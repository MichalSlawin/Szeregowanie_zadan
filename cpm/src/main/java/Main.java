import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.alg.shortestpath.*;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Graph<Task, DefaultEdge> g = new DirectedMultigraph<>(DefaultEdge.class);

        Task task1 = new Task("task1", 2);
        Task task2 = new Task("task2", 3);
        Task task3 = new Task("task3", 4);
        Task task4 = new Task("task4", 2);

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

        AllDirectedPaths<Task, DefaultEdge> paths = new AllDirectedPaths<>(g);
        List<GraphPath<Task, DefaultEdge>> pathsList = paths.getAllPaths(task1, task4, true, null);

        for(GraphPath<Task, DefaultEdge> graphPath : pathsList) {
            System.out.println("Path: ");
            List<DefaultEdge> edges = graphPath.getEdgeList();
            for(DefaultEdge edge : edges) {
                System.out.println(edge);
            }
        }

    }
}
