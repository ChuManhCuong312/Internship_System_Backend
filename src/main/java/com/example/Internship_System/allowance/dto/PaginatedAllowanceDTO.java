package com.example.Internship_System.allowance.dto;

import java.util.List;

public class PaginatedAllowanceDTO {
    private List<AllowanceDTO> data;
    private int totalAllowances;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    public PaginatedAllowanceDTO() {
    }

    public PaginatedAllowanceDTO(List<AllowanceDTO> data, int totalAllowances, int currentPage, int pageSize) {
        this.data = data;
        this.totalAllowances = totalAllowances;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalAllowances / pageSize);
    }

    public List<AllowanceDTO> getData() {
        return data;
    }

    public void setData(List<AllowanceDTO> data) {
        this.data = data;
    }

    public int getTotalAllowances() {
        return totalAllowances;
    }

    public void setTotalAllowances(int totalAllowances) {
        this.totalAllowances = totalAllowances;
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
