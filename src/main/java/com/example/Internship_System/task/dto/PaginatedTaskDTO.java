package com.example.Internship_System.task.dto;

import java.util.List;

public class PaginatedTaskDTO {
    private List<TaskDTO> data;
    private int totalTasks;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    public PaginatedTaskDTO() {
    }

    public PaginatedTaskDTO(List<TaskDTO> data, int totalTasks, int currentPage, int pageSize) {
        this.data = data;
        this.totalTasks = totalTasks;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalTasks / pageSize);
    }

    public List<TaskDTO> getData() {
        return data;
    }

    public void setData(List<TaskDTO> data) {
        this.data = data;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
