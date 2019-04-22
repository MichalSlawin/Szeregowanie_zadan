package org.graph;

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

    Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
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

    @Override
    public String toString() {
        String str = this.name + "(" + this.duration + ")" + "("
                        + this.earliestStart + "/" + this.earliestFinish + ")"
                + "(" + this.latestStart + "/" + this.latestFinish + ")";

//        if(this.isEnd) {
//            str = str.concat(" END");
//        }
//        if(this.isStart) {
//            str = str.concat(" START");
//        }
        return str;
    }
}
