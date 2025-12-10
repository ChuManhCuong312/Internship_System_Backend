package com.example.Internship_System.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaskUpdateRequest {
    // Task info
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime deadline;
    private int programId;
    private int mentorId;
    private Integer assignedBy;
    private boolean dueSoon;
    
    // Related data
    private List<Integer> teamIds;          // Danh sách team được giao task
    private List<String> fileLinks;         // Danh sách link file đính kèm
    private Integer progressPercent;        // Phần trăm hoàn thành (0-100)
    private String progressNote;            // Ghi chú tiến độ
    private List<Integer> tagIds;           // Danh sách tag IDs
}
