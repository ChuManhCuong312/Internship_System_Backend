package com.example.Internship_System.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PaginatedTaskDTO {
    private List<TaskDTO> data;
    private int totalTasks;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    @SuppressWarnings("unused")
    public PaginatedTaskDTO() {
    }

    public PaginatedTaskDTO(List<TaskDTO> data, int totalTasks, int currentPage, int pageSize) {
        this.data = data;
        this.totalTasks = totalTasks;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalTasks / pageSize);
    }

}
