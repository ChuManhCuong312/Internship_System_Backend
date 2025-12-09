package com.example.Internship_System.task.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskStatisticsDTO {
    private int inProgress;
    private int todo;
    private int done;
    private int total;
@SuppressWarnings("unused")
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

}
