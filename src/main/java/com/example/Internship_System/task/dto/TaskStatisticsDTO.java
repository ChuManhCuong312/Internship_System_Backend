package com.example.Internship_System.task.dto;

public class TaskStatisticsDTO {
    private int inProgress;
    private int todo;
    private int done;
    private int total;

    public TaskStatisticsDTO() {
        this.inProgress = 0;
        this.todo = 0;
        this.done = 0;
        this.total = 0;
    }

    public TaskStatisticsDTO(int inProgress, int todo, int done, int total) {
        this.inProgress = inProgress;
        this.todo = todo;
        this.done = done;
        this.total = total;
    }

    public int getInProgress() {
        return inProgress;
    }

    public void setInProgress(int inProgress) {
        this.inProgress = inProgress;
    }

    public int getTodo() {
        return todo;
    }

    public void setTodo(int todo) {
        this.todo = todo;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
