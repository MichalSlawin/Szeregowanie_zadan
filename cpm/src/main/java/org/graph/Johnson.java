package org.graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Johnson {
    private static int MACHINES_COUNT = 3;
    private List<Machine> machines;
    private List<Task> tasks;
    private int tasksCount = 0;
    private List<Integer> n1;
    private List<Task> n1Tasks;
    private List<Integer> n2;
    private List<Task> n2Tasks;
    private List<Task> sortedTasks;

    public Johnson(String filename) throws Exception {
        machines = new ArrayList<>();
        tasks = new ArrayList<>();

        try {
            init(filename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        if(!isSecondMachineDominated()) {
            throw new Exception("Druga maszyna nie jest zdominowana!");
        }
        countN1AndN2();
        createTimeTable();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Machine> getMachines() {
        return machines;
    }

    public List<Integer> getN1() {
        return n1;
    }

    public List<Integer> getN2() {
        return n2;
    }

    public List<Task> getN1Tasks() {
        return n1Tasks;
    }

    public List<Task> getN2Tasks() {
        return n2Tasks;
    }

    public List<Task> getSortedTasks() {
        return sortedTasks;
    }

    private void init(String fileName) throws Exception {
        String line;
        int lines = 0;
        int duration = 0;

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                int taskNum = 0;
                lines++;
                String numberStr = line.substring(0, line.indexOf(' '));
                String restOfNums = line.substring(line.indexOf(' ')+1);

                List<Integer> durations = new ArrayList<>();
                try {
                    duration = Integer.valueOf(numberStr);
                } catch(NumberFormatException e) {
                    System.out.println("Zly format pliku");
                    System.exit(-1);
                }
                durations.add(duration);
                if(lines == 1) {
                    this.tasksCount++;
                    tasks.add(new Task("Z" + this.tasksCount));
                }
                this.tasks.get(taskNum++).addDuration(duration);

                while(restOfNums != null) {
                    if(lines == 1) {
                        this.tasksCount++;
                        tasks.add(new Task("Z" + this.tasksCount));
                    }
                    if(restOfNums.indexOf(' ') != -1) {
                        numberStr = restOfNums.substring(0, restOfNums.indexOf(' '));
                        restOfNums = restOfNums.substring(restOfNums.indexOf(' ')+1);

                    } else {
                        numberStr = restOfNums;
                        restOfNums = null;
                    }
                    try {
                        duration = Integer.valueOf(numberStr);
                    } catch(NumberFormatException e) {
                        System.out.println("Zly format pliku");
                        System.exit(-1);
                    }

                    durations.add(duration);
                    this.tasks.get(taskNum++).addDuration(duration);
                }
                Machine machine = new Machine(lines, durations);
                this.machines.add(machine);
            }
            bufferedReader.close();

            if(lines != MACHINES_COUNT) {
                throw new Exception("Liczba maszyn wynosi " + lines + ", a powinna " + MACHINES_COUNT);
            }
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }

    private boolean isSecondMachineDominated() {
        boolean dominated = true;

        for(int i = 0; i < tasksCount; i++) {
            int mach1TaskDur = machines.get(0).getTasksDurations().get(i);
            for(int j = 0; j < tasksCount; j++) {
                int mach2TaskDur = machines.get(1).getTasksDurations().get(i);
                if(mach2TaskDur > mach1TaskDur) {
                    dominated = false;
                }
            }
        }
        if(!dominated) {
            dominated = true;
            for(int i = 0; i < tasksCount; i++) {
                int mach3TaskDur = machines.get(2).getTasksDurations().get(i);
                for(int j = 0; j < tasksCount; j++) {
                    int mach2TaskDur = machines.get(1).getTasksDurations().get(j);
                    if(mach2TaskDur > mach3TaskDur) {
                        dominated = false;
                    }
                }
            }
        }

        return dominated;
    }

    private void countN1AndN2() {
        this.n1 = new ArrayList<>();
        this.n2 = new ArrayList<>();
        this.n1Tasks = new ArrayList<>();
        this.n2Tasks = new ArrayList<>();

        for(int i = 0; i < tasksCount; i++) {
            int mach1TaskDur = machines.get(0).getTasksDurations().get(i);
            int mach2TaskDur = machines.get(1).getTasksDurations().get(i);
            int mach3TaskDur = machines.get(2).getTasksDurations().get(i);

            if(mach1TaskDur + mach2TaskDur < mach2TaskDur + mach3TaskDur) {
                insertAndSort(n1,mach1TaskDur + mach2TaskDur, true, i, n1Tasks);
            } else {
                insertAndSort(n2,mach3TaskDur + mach2TaskDur, false, i, n2Tasks);
            }
        }
        this.sortedTasks = new ArrayList<>(n1Tasks);
        sortedTasks.addAll(n2Tasks);
    }

    private void insertAndSort(List<Integer> nList, int el, boolean asc, int taskNum, List<Task> sortedTasks) {
        boolean added = false;

        if(nList.isEmpty()) {
            nList.add(el);
            sortedTasks.add(this.tasks.get(taskNum));
        } else if(!asc) {
            for(int i = 0; i < nList.size(); i++) {
                if(el >= nList.get(i) && !added) {
                    nList.add(i, el);
                    sortedTasks.add(i, this.tasks.get(taskNum));
                    added = true;
                }
            }
        } else if(asc) {
            for(int i = 0; i < nList.size(); i++) {
                if(el <= nList.get(i) && !added) {
                    nList.add(i, el);
                    sortedTasks.add(i, this.tasks.get(taskNum));
                    added = true;
                }
            }
            if(!added) {
                nList.add(el);
            }
        }
    }

    private void createTimeTable() {
        for(Task task : this.sortedTasks) {
            int waitTime = 0;
            for(Machine machine : this.machines) {
                waitTime = machine.addTaskJohnson(task, waitTime);
            }
        }
    }

    public String getTimeTable() {
        String str = "Harmonogram:\n";

        for(Machine machine : this.machines) {
            str = str.concat("M" + machine.getNumber() + machine.getTasks() + "\n");
        }
        return str;
    }

}
