package org.graph;

import static org.graph.GraphTests.*;

public class Main {
    private final static String JOHNSON_FILENAME = "inputFiles/johnson.txt";
    private final static String JOHNSON_FILENAME2 = "inputFiles/johnson2.txt";

    public static void main(String[] args) {
        johnsonTest();
    }

    private static void johnsonTest() {
        Johnson johnson = null;
        try {
            johnson = new Johnson(JOHNSON_FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println(johnson.getMachines());
        System.out.println(johnson.getTasks());
        System.out.println(johnson.getN1());
        System.out.println(johnson.getN2());
        System.out.println(johnson.getSortedTasks());
        System.out.println(johnson.getTimeTable());
    }
}