package org.graph;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String name;
    private int duration;
    private boolean isStart = false;
    private boolean isEnd = false;
    private boolean isCritical = false;
    private int earliestStart = -1;
    private int latestStart = -1;
    private int earliestFinish = -1;
    private int latestFinish = -1;
    private boolean isFinished = false;
    private int startTime = -1;
    private int endTime = -1;
    private int depth = -1;
    private int expectedEndTime = -1;
    private int modifiedExpectedEndTime = 1;
    private List<Integer> durationsList;

    Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    Task(String name, int duration, int expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
        this.name = name;
        this.duration = duration;
    }

    Task(String name) {
        this.name = name;
        this.durationsList = new ArrayList<>();
    }

    public void addDuration(int duration) {
        this.durationsList.add(duration);
    }

    public List<Integer> getDurationsList() {
        return durationsList;
    }

    public void setDurationsList(List<Integer> durationsList) {
        this.durationsList = durationsList;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getExpectedEndTime() {
        return expectedEndTime;
    }

    public void setExpectedEndTime(int expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    public int getModifiedExpectedEndTime() {
        return modifiedExpectedEndTime;
    }

    public void setModifiedExpectedEndTime(int modifiedExpectedEndTime) {
        this.modifiedExpectedEndTime = modifiedExpectedEndTime;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    int getDuration() {
        return duration;
    }

    void setDuration(int duration) {
        this.duration = duration;
    }

    boolean isStart() {
        return isStart;
    }

    void setStart(boolean start) {
        isStart = start;
    }

    boolean isEnd() {
        return isEnd;
    }

    void setEnd(boolean end) {
        isEnd = end;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public void setCritical(boolean critical) {
        isCritical = critical;
    }

    public int getEarliestStart() {
        return earliestStart;
    }

    public int getLatestStart() {
        return latestStart;
    }

    public int getEarliestFinish() {
        return earliestFinish;
    }

    void setEarliestFinish(int earliestFinish) {
        this.earliestFinish = earliestFinish;
        this.earliestStart = this.earliestFinish - this.duration;
    }

    void setEarliestStart(int earliestStart) {
        this.earliestStart = earliestStart;
        this.earliestFinish = this.earliestStart + this.duration;
    }

    public int getLatestFinish() {
        return latestFinish;
    }

    void setLatestFinish(int latestFinish) {
        this.latestFinish = latestFinish;
        this.latestStart = this.latestFinish - this.duration;
    }

    void setLatestStart(int latestStart) {
        this.latestStart = latestStart;
        this.latestFinish = this.latestStart + this.duration;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        String str = this.name;
        // + "(dur:" + this.duration + ")\n(dep:" + this.depth + ")";
        if(this.earliestStart != -1) {
            str = str.concat("\n("
                    + this.earliestStart + "/" + this.earliestFinish + ")\n"
                    + "(" + this.latestStart + "/" + this.latestFinish + ")");
        } else if(this.startTime != -1) {
            str = str.concat("(" + this.startTime + "/" + this.endTime + ")");
        }
        if(this.expectedEndTime != -1 && this.modifiedExpectedEndTime != 1) {
            str = str.concat("\n(" + this.expectedEndTime + ")(" + this.modifiedExpectedEndTime + ")");
        }

        if(this.isCritical) {
            str = str.concat("\nCRITICAL");
        }

        return str;
    }
}
