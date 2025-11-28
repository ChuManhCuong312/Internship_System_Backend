package com.example.Internship_System.task.dto;

import java.time.LocalDateTime;

public class TaskDTO {
    private int taskId;
    private int programId;
    private String title;
    private String description;
    private String assignedBy;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private boolean dueSoon;
    private String priority;
    private int mentorId;

    public TaskDTO() {}

    public TaskDTO(int taskId, int programId, String title, String description, 
                   String assignedBy, String status, LocalDateTime createdAt, 
                   LocalDateTime deadline, boolean dueSoon, String priority, int mentorId) {
        this.taskId = taskId;
        this.programId = programId;
        this.title = title;
        this.description = description;
        this.assignedBy = assignedBy;
        this.status = status;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.dueSoon = dueSoon;
        this.priority = priority;
        this.mentorId = mentorId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public boolean isDueSoon() {
        return dueSoon;
    }

    public void setDueSoon(boolean dueSoon) {
        this.dueSoon = dueSoon;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }
}
