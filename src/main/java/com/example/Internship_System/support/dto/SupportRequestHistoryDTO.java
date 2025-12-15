package com.example.Internship_System.support.dto;

import java.time.LocalDateTime;
import com.example.Internship_System.support.entity.SupportStatus;

public record SupportRequestHistoryDTO(
        Integer historyId,
        SupportStatus oldStatus,
        SupportStatus newStatus,
        LocalDateTime changeDate,
        Integer changedBy,
        String changedByName,
        String remarks) {
}
