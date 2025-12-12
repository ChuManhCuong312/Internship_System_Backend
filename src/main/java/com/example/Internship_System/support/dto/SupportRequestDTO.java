package com.example.Internship_System.support.dto;

import java.time.LocalDateTime;

import com.example.Internship_System.support.entity.SupportStatus;
import com.example.Internship_System.support.entity.SupportType;

public record SupportRequestDTO(
        Integer supportId,
        Integer internId,
        SupportType supportType,
        String title,
        String description,
        SupportStatus status,
        String response,
        String rejectionReason,
        Integer processedBy,
        LocalDateTime processedDate,
        LocalDateTime createdAt,
        String fullName) {
}