package com.example.Internship_System.hr.dto;

import com.example.Internship_System.intern.entity.InternConfirmStatus;

import java.time.LocalDateTime;

public class InternAssignmentViewDTO {
    private Integer internId;
    private String internName;
    private String internConfirmStatus;
    private Integer mentorId;
    private String mentorName;
    private LocalDateTime assignedAt;

    public InternAssignmentViewDTO() {}

    public InternAssignmentViewDTO(Integer internId, String internName, InternConfirmStatus internConfirmStatus,
                                   Integer mentorId, String mentorName, LocalDateTime assignedAt) {
        this.internId = internId;
        this.internName = internName;
        this.internConfirmStatus = internConfirmStatus != null ? internConfirmStatus.name() : null;
        this.mentorId = mentorId;
        this.mentorName = mentorName;
        this.assignedAt = assignedAt;
    }

    // getters / setters
    public Integer getInternId() { return internId; }
    public void setInternId(Integer internId) { this.internId = internId; }

    public String getInternName() { return internName; }
    public void setInternName(String internName) { this.internName = internName; }

    public String getInternConfirmStatus() { return internConfirmStatus; }
    public void setInternConfirmStatus(String internConfirmStatus) { this.internConfirmStatus = internConfirmStatus; }

    public Integer getMentorId() { return mentorId; }
    public void setMentorId(Integer mentorId) { this.mentorId = mentorId; }

    public String getMentorName() { return mentorName; }
    public void setMentorName(String mentorName) { this.mentorName = mentorName; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
