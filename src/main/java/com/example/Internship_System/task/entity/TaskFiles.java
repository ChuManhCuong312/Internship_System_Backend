package com.example.Internship_System.task.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tasks_files")
@Getter
@Setter
public class TaskFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_files_id")
    private int taskFilesId;

    @Column(name = "task_id", nullable = false)
    private int taskId;

    @Column(name = "link_file", nullable = false)
    @NotBlank(message = "Đường dẫn file là bắt buộc")
    private String linkFile;

    @SuppressWarnings("unused")
    public TaskFiles() {}

    @SuppressWarnings("unused")
    public TaskFiles(int taskId, String linkFile) {
        this.taskId = taskId;
        this.linkFile = linkFile;
    }
}
