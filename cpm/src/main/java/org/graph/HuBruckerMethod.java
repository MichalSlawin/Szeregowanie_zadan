package org.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static sun.swing.MenuItemLayoutHelper.max;

public class HuBruckerMethod {
    private int machinesNum;
    private TasksGraph tasksGraph;
    private List<Machine> machines;
    private int currTime = 0;
    private boolean inTree;
    private int maxModifiedExpectedEndTime;
    private Task endTask;

    public HuBruckerMethod(int machinesNum, TasksGraph tasksGraph) throws Exception {
        this.machinesNum = machinesNum;
        this.tasksGraph = tasksGraph;
        isTree();

        this.machines = new ArrayList<>();
        for(int i = 0; i < machinesNum; i++) {
            this.machines.add(new Machine(i));
        }
    }

    private void isTree() throws Exception {
        int startsNum = this.tasksGraph.getStartTasks().size();
        int endTasks = this.tasksGraph.getEndTasks().size();

        if(startsNum == 1) {
            this.inTree = false;
        } else if(endTasks == 1) {
            this.inTree = true;
        } else {
            throw new Exception("Graf nie jest drzewem!");
        }
    }

    public void bruckerMethod() throws Exception {
        List<Task> endTasks = tasksGraph.getEndTasks();
        if(endTasks.size() == 1) {
            Task endTask = endTasks.get(0);
            this.endTask = endTask;
            endTask.setModifiedExpectedEndTime(1-endTask.getExpectedEndTime());
            this.maxModifiedExpectedEndTime = endTask.getModifiedExpectedEndTime();

            setModifiedExpectedEndTimes(endTask);
            bruckerSetTimeTable();
        } else {
            throw new Exception("Graf nie jest drzewem wchodzacym!");
        }
    }

    private void setModifiedExpectedEndTimes(Task task) {
        Graph<Task, DefaultEdge> graph = this.tasksGraph.getGraph();
        Set<DefaultEdge> edges = graph.incomingEdgesOf(task);

        for(DefaultEdge edge : edges) {
            Task source = graph.getEdgeSource(edge);
            source.setModifiedExpectedEndTime(max(1+task.getModifiedExpectedEndTime(), 1-source.getExpectedEndTime()));
            if(this.maxModifiedExpectedEndTime < source.getModifiedExpectedEndTime()) {
                this.maxModifiedExpectedEndTime = source.getModifiedExpectedEndTime();
            }
            setModifiedExpectedEndTimes(source);
        }
    }

    public void bruckerSetTimeTable() {
        Set<Task> allTasks = this.tasksGraph.getGraph().vertexSet();
        int allTasksNum = allTasks.size();
        int addedTasks = 0;

        while(addedTasks < allTasksNum) {
            Task task = bruckerFindTask();

            int earliestStart = getTaskEarliestStart(task);
            int bestMachineNum = 0;

            for(Machine machine : this.machines) {
                if(machine.getFreeOnTime() >= earliestStart && !task.isFinished()) {
                    Machine bestMachine = getMachineByNumber(bestMachineNum);
                    if(bestMachine != null) {
                        if(machine.getFreeOnTime() < bestMachine.getFreeOnTime()) {
                            bestMachineNum = machine.getNumber();
                        }
                    } else {
                        System.out.println("Nie znaleziono maszyny o indeksie " + bestMachineNum);
                    }
                }
            }
            Machine bestMachine = getMachineByNumber(bestMachineNum);
            if(bestMachine != null) {
                bestMachine.addTaskHu(task);
                task.setFinished(true);
                addedTasks++;
            } else {
                System.out.println("Nie znaleziono maszyny dla zadania " + task);
            }
        }
        tasksGraph.setTimeTable(this.machines);
    }

    private Task bruckerFindTask() {
        Set<Task> allTasks = this.tasksGraph.getGraph().vertexSet();
        Task foundTask = this.endTask;

        for(Task task : allTasks) {
            if(isTaskFree(task) && !task.isFinished() && task.getModifiedExpectedEndTime() > foundTask.getModifiedExpectedEndTime()) {
                foundTask = task;
            }
        }
        return foundTask;
    }

    public void huMethod() {
        if(!this.inTree) {
            this.tasksGraph.reverseTree();
            System.out.println("odwracam drzewo");
        }
        try {
            this.tasksGraph.setTasksDepth();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Set<Task> allTasks = this.tasksGraph.getGraph().vertexSet();
        int allTasksNum = allTasks.size();
        int addedTasks = 0;

        while(addedTasks < allTasksNum) {
            int currDepth = 0;
            for(Task task : allTasks) {
                if(isTaskFree(task) && !task.isFinished() && task.getDepth() > currDepth) {
                    currDepth = task.getDepth();
                }
            }
            for(Task task : allTasks) {
                if(isTaskFree(task) && !task.isFinished() && task.getDepth() == currDepth) {
                    int earliestStart = getTaskEarliestStart(task);
                    int bestMachineNum = 0;

                    for(Machine machine : this.machines) {
                        if(machine.getFreeOnTime() >= earliestStart && !task.isFinished()) {
                            Machine bestMachine = getMachineByNumber(bestMachineNum);
                            if(bestMachine != null) {
                                if(machine.getFreeOnTime() < bestMachine.getFreeOnTime()) {
                                    bestMachineNum = machine.getNumber();
                                }
                            } else {
                                System.out.println("Nie znaleziono maszyny o indeksie " + bestMachineNum);
                            }
                        }
                    }
                    Machine bestMachine = getMachineByNumber(bestMachineNum);
                    if(bestMachine != null) {
                        bestMachine.addTaskHu(task);
                        task.setFinished(true);
                        addedTasks++;
                    } else {
                        System.out.println("Nie znaleziono maszyny dla zadania " + task);
                    }
                }
            }
        }
        tasksGraph.setTimeTable(this.machines);
        if(!this.inTree) {
            tasksGraph.reverseTimeTable();
        }
    }

    private boolean isTaskFree(Task task) {
        Set<DefaultEdge> inEdges = this.tasksGraph.getGraph().incomingEdgesOf(task);

        for(DefaultEdge edge : inEdges) {
            if(!this.tasksGraph.getGraph().getEdgeSource(edge).isFinished()) {
                return false;
            }
        }
        return true;
    }

    private Machine getMachineByNumber(int num) {
        for(Machine machine : this.machines) {
            if(machine.getNumber() == num) {
                return machine;
            }
        }
        return null;
    }

    private int getTaskEarliestStart(Task task) {
        int maxEnd = 0;
        Set<DefaultEdge> inEdges = this.tasksGraph.getGraph().incomingEdgesOf(task);

        for(DefaultEdge edge : inEdges) {
            int sourceEnd = this.tasksGraph.getGraph().getEdgeSource(edge).getEndTime();
            if(sourceEnd > maxEnd) {
                maxEnd = sourceEnd;
            }
        }
        return maxEnd;
    }
}
