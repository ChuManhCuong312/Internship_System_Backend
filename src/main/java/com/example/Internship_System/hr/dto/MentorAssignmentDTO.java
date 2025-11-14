package com.example.Internship_System.hr.dto;

import java.time.LocalDateTime;

public class MentorAssignmentDTO {
    private Integer assignmentId;
    private String mentorName;
    private String internName;
    private LocalDateTime assignedAt;

    public MentorAssignmentDTO() {}

    public MentorAssignmentDTO(Integer assignmentId, String mentorName, String internName, LocalDateTime assignedAt) {
        this.assignmentId = assignmentId;
        this.mentorName = mentorName;
        this.internName = internName;
        this.assignedAt = assignedAt;
    }

    public Integer getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Integer assignmentId) { this.assignmentId = assignmentId; }

    public String getMentorName() { return mentorName; }
    public void setMentorName(String mentorName) { this.mentorName = mentorName; }

    public String getInternName() { return internName; }
    public void setInternName(String internName) { this.internName = internName; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
