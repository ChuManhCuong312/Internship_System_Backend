package com.example.Internship_System.allowance.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PaginatedAllowanceDTO {
    private List<AllowanceDTO> data;
    private int totalAllowances;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    @SuppressWarnings("unused")
    public PaginatedAllowanceDTO() {
    }

    public PaginatedAllowanceDTO(List<AllowanceDTO> data, int totalAllowances, int currentPage, int pageSize) {
        this.data = data;
        this.totalAllowances = totalAllowances;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalAllowances / pageSize);
    }

}
