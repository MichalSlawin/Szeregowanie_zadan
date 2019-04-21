package org.graph;

class GraphTests {
    private final static String FILENAME1 = "inputFiles/input1.txt";
    static void createGraphTest() {
        TasksGraph tasksGraph = new TasksGraph(FILENAME1);
        System.out.println(tasksGraph.getGraph());
        System.out.println(tasksGraph.getCriticalPath());
        System.out.println(tasksGraph.getCriticalPath().getDuration());
    }
}
