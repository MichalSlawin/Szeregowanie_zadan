package org.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.util.Scanner;

class GraphTests {
    private final static String FILENAME1 = "inputFiles/input1.txt";
    private final static String FILENAME2 = "inputFiles/inputZajecia.txt";
    private final static String FILENAME3 = "inputFiles/input2.txt";
    private final static String HUFILENAME1 = "inputFiles/huinput1.txt";
    private final static String HUFILENAME2 = "inputFiles/huinput2.txt";
    private final static String HUFILENAME3 = "inputFiles/huinput3.txt";
    private final static String BRUCKERFILENAME1 = "inputFiles/inputBrucker.txt";
    private final static int MACHINES_NUM = 4;

    static void bruckerTest() {
        TasksGraph tasksGraph = new TasksGraph(BRUCKERFILENAME1, true, true);
        HuBruckerMethod huBruckerMethod = null;
        try {
            huBruckerMethod = new HuBruckerMethod(MACHINES_NUM, tasksGraph);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        try {
            huBruckerMethod.bruckerMethod();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println(tasksGraph.getTimeTableString());
        drawGraph(tasksGraph.getGraph());
    }

    static void huTest() {
        TasksGraph tasksGraph = new TasksGraph(HUFILENAME1, true, false);
        HuBruckerMethod huBruckerMethod = null;
        try {
            huBruckerMethod = new HuBruckerMethod(MACHINES_NUM, tasksGraph);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        huBruckerMethod.huMethod();

        System.out.println(tasksGraph.getTimeTableString());
        drawGraph(tasksGraph.getGraph());
    }

    static void cpmTest() {
        Scanner input = new Scanner(System.in);
        String fileName;
//        System.out.println("Wybierz input: ");
//        int choice = input.nextInt();
        int choice = 3;
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
        TasksGraph tasksGraph = new TasksGraph(fileName, false, false);
        tasksGraph.cpm();

        System.out.println(tasksGraph.getTimeTableString());
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
