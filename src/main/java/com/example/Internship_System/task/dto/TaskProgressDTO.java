package com.example.Internship_System.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskProgressDTO {
    private int progressId;
    private int taskId;
    private int percentComplete;
    private String note;
    private LocalDateTime updatedAt;
@SuppressWarnings("unused")
    public TaskProgressDTO() {}

    public TaskProgressDTO(int progressId, int taskId, int percentComplete, String note, LocalDateTime updatedAt) {
        this.progressId = progressId;
        this.taskId = taskId;
        this.percentComplete = percentComplete;
        this.note = note;
        this.updatedAt = updatedAt;
    }

}
