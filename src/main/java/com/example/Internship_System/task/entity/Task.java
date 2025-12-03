package com.example.Internship_System.task.entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
    private Integer assignedBy;
    @Pattern(regexp = "^(TODO|IN_PROGRESS|DONE|REVIEWED)?$",
            message = "Trạng thái phải hợp lệ")
    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
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
}
