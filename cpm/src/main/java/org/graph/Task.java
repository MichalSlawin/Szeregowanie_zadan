package org.graph;

public class Task {
    private String name;
    private int duration;
    private boolean isStart = false;
    private boolean isEnd = false;

    Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public String toString() {
        String str = this.name + "(" + this.duration + ")";
//        if(this.isEnd) {
//            str = str.concat(" END");
//        }
//        if(this.isStart) {
//            str = str.concat(" START");
//        }
        return str;
    }
}
