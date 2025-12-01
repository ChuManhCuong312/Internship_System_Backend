package com.example.Internship_System.task.dto;

import java.time.LocalDateTime;

public class TaskProgressDTO {
    private int progressId;
    private int taskId;
    private int percentComplete;
    private String note;
    private LocalDateTime updatedAt;

    public TaskProgressDTO() {}

    public TaskProgressDTO(int progressId, int taskId, int percentComplete, String note, LocalDateTime updatedAt) {
        this.progressId = progressId;
        this.taskId = taskId;
        this.percentComplete = percentComplete;
        this.note = note;
        this.updatedAt = updatedAt;
    }

    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
