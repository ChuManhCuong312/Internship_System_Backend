package com.example.Internship_System.task.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskFilesDTO {
    private int taskFilesId;
    private int taskId;
    private String linkFile;

    @SuppressWarnings("unused")
    public TaskFilesDTO() {}

    public TaskFilesDTO(int taskFilesId, int taskId, String linkFile) {
        this.taskFilesId = taskFilesId;
        this.taskId = taskId;
        this.linkFile = linkFile;
    }

}
