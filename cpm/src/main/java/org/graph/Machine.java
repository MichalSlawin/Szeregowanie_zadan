package org.graph;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.fill;

public class Machine {
    private int number;
    private int cMax;
    private boolean[] occupied;
    private List<Task> tasks;
    private int freeOnTime = 0;

    public Machine(int cMax, int number) {
        this.tasks = new ArrayList<>();
        this.cMax = cMax + 1;
        this.occupied = new boolean[this.cMax];
        fill(this.occupied, 0, this.cMax, false);
        this.number = number;
    }

    public Machine(int number) {
        this.tasks = new ArrayList<>();
        this.number = number;
    }

    public boolean isOccupied(int startInd, int endInd) {
        for(int i = startInd; i <= endInd; i++) {
            if(this.occupied[i]) {
                return true;
            }
        }
        return false;
    }

    public int getNumber() {
        return number;
    }

    public int getcMax() {
        return cMax;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public int getFreeOnTime() {
        return freeOnTime;
    }

    public void setTasks(List<Task> tasks) {
        for(Task task : tasks) {
            addTaskCPM(task);
        }
    }

    public void addTaskCPM(Task task) {
        this.tasks.add(task);
        for(int i = task.getEarliestStart(); i < task.getLatestFinish(); i++) {
            this.occupied[i] = true;
        }
    }

    public void addTaskHu(Task task) {
        task.setStartTime(this.freeOnTime);
        this.tasks.add(task);
        this.freeOnTime += task.getDuration();
        task.setEndTime(this.freeOnTime);
    }

    @Override
    public String toString() {
        List<String> tasksStr = new ArrayList<>();
        for(Task task : this.tasks) {
            if(task.getEarliestStart() != -1) {
                tasksStr.add(task.getName() + "(" + task.getEarliestStart() + "/" + task.getLatestFinish() + ")");
            } else {
                tasksStr.add(task.getName() + "(" + task.getStartTime() + "/" + task.getEndTime() + ")");
            }
        }
        return "M" + this.number + ": " + tasksStr;
    }
}
