package com.example.Internship_System.support.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public record TablePaging<T>(
        List<T> data,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last) {
    public TablePaging(Page<T> pageData) {
        this(
                pageData.getContent(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages(),
                pageData.isLast());
    }
}
