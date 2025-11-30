package com.example.Internship_System.task.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tasks_files")
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

    public TaskFiles() {}

    public TaskFiles(int taskId, String linkFile) {
        this.taskId = taskId;
        this.linkFile = linkFile;
    }

    public int getTaskFilesId() {
        return taskFilesId;
    }

    public void setTaskFilesId(int taskFilesId) {
        this.taskFilesId = taskFilesId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getLinkFile() {
        return linkFile;
    }

    public void setLinkFile(String linkFile) {
        this.linkFile = linkFile;
    }
}
