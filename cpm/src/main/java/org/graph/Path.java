package org.graph;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;

public class Path {
    private Task startTask;
    private Task endTask;
    private GraphPath<Task, DefaultEdge> path;
    private int duration;

    Path(Task startTask, Task endTask, GraphPath<Task, DefaultEdge> path, int duration) {
        this.startTask = startTask;
        this.endTask = endTask;
        this.path = path;
        this.duration = duration;
    }

    Task getStartTask() {
        return startTask;
    }

    void setStartTask(Task startTask) {
        this.startTask = startTask;
    }

    Task getEndTask() {
        return endTask;
    }

    void setEndTask(Task endTask) {
        this.endTask = endTask;
    }

    GraphPath<Task, DefaultEdge> getPath() {
        return path;
    }

    void setPath(GraphPath<Task, DefaultEdge> path) {
        this.path = path;
    }

    int getDuration() {
        return duration;
    }

    void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return this.path.toString();
    }
}
