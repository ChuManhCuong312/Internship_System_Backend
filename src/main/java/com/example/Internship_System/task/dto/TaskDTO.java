package com.example.Internship_System.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskDTO {
    private int taskId;
    private int programId;
    private String programName;
    private String title;
    private String description;
    private String assignedBy;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private boolean dueSoon;
    private String priority;
    private int mentorId;
    private String mentorName;

    public TaskDTO() {}

    @SuppressWarnings("unused")
    public TaskDTO(int taskId, int programId, String programName, String title, String description, 
                   String assignedBy, String status, LocalDateTime createdAt, 
                   LocalDateTime deadline, boolean dueSoon, String priority, int mentorId, String mentorName) {
        this.taskId = taskId;
        this.programId = programId;
        this.programName = programName;
        this.title = title;
        this.description = description;
        this.assignedBy = assignedBy;
        this.status = status;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.dueSoon = dueSoon;
        this.priority = priority;
        this.mentorId = mentorId;
        this.mentorName = mentorName;
    }

}
