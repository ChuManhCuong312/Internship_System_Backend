package com.example.Internship_System.task.dto;

public class TaskFilesDTO {
    private int taskFilesId;
    private int taskId;
    private String linkFile;

    public TaskFilesDTO() {}

    public TaskFilesDTO(int taskFilesId, int taskId, String linkFile) {
        this.taskFilesId = taskFilesId;
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
