package org.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.util.Scanner;

class GraphTests {
    private final static String FILENAME1 = "inputFiles/input1.txt";
    private final static String FILENAME2 = "inputFiles/inputZajecia.txt";
    private final static String FILENAME3 = "inputFiles/input2.txt";

    static void createGraphTest() {
        Scanner input = new Scanner(System.in);
        String fileName;
//        System.out.println("Wybierz input: ");
//        int choice = input.nextInt();
        int choice = 2;
        switch(choice) {
            case 1:
                fileName = FILENAME1;
                break;
            case 2:
                fileName = FILENAME2;
                break;
            case 3:
                fileName = FILENAME3;
                break;
            default :
                return;
        }
        TasksGraph tasksGraph = new TasksGraph(fileName);
//        System.out.println("Graph:");
//        System.out.println(tasksGraph.getGraph());
//        System.out.println("Critical path:");
//        System.out.println(tasksGraph.getCriticalPath());
//        System.out.println("Tasks:");
//        System.out.println(tasksGraph.getGraph().vertexSet());
//        System.out.println("Critical tasks:");
//        System.out.println(tasksGraph.getCriticalTasks());
//        System.out.println("Cmax:");
//        System.out.println(tasksGraph.getcMax());

        drawGraph(tasksGraph.getGraph());
    }

    private static void drawGraph(Graph<Task, DefaultEdge> graph)
    {
        JGraphXAdapter applet = new JGraphXAdapter(graph);
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("Tasks graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
