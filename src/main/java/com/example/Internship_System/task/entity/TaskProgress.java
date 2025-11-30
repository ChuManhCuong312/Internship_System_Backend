package com.example.Internship_System.task.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_progress")
public class TaskProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private int progressId;

    @Column(name = "task_id", nullable = false)
    private int taskId;

    @Column(name = "percent_complete")
    @Min(value = 0, message = "Phần trăm hoàn thành phải từ 0 đến 100")
    @Max(value = 100, message = "Phần trăm hoàn thành phải từ 0 đến 100")
    private int percentComplete;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TaskProgress() {}

    public TaskProgress(int taskId, int percentComplete, String note) {
        this.taskId = taskId;
        this.percentComplete = percentComplete;
        this.note = note;
        this.updatedAt = LocalDateTime.now();
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
