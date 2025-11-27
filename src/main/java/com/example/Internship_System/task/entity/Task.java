package com.example.Internship_System.task.entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    private int taskId;
    @Column(name = "program_id",nullable = false)
    private int programId;
    @Column(name = "title", nullable = false)
    @NotBlank(message = "Tiêu đề là bắt buộc")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "assigned_by", nullable = false)
    private String assignedBy;
    @Pattern(regexp = "^(TODO|IN_PROGRESS|DONE|REVIEWED)?$",
            message = "Trạng thái phải hợp lệ")
    @Column(name = "status")
    private String status;
    @Column(name = "create_at")
    private LocalDateTime created_at;
    @Column(name = "deadline")
    @NotNull(message ="Deadline là bắt buộc")
    private LocalDateTime deadline;
    @Column(name ="due_soon")
    private boolean due_soon;

    @Pattern(regexp = "^(LOW|MEDIUM|HIGH)?$",
            message = "Độ ưu tiên phải hợp lệ")
    @Column(name ="priority")
    private String priority;
    @Column(name = "mentorId", nullable = false)

    private int mentorId;
    @Column(name = "internId", nullable = false)
    private int internId;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public int getInternId() {
        return internId;
    }

    public void setInternId(int internId) {
        this.internId = internId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public boolean isDue_soon() {
        return due_soon;
    }

    public void setDue_soon(boolean due_soon) {
        this.due_soon = due_soon;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
