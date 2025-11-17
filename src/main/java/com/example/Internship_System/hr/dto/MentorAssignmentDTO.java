package com.example.Internship_System.hr.dto;

import java.time.LocalDateTime;

public class MentorAssignmentDTO {

    private Integer assignmentId;
    private Integer internId;
    private String internName;
    private Integer mentorId;
    private String mentorName;
    private LocalDateTime assignedAt;

    public MentorAssignmentDTO() {}

    public MentorAssignmentDTO(Integer assignmentId, Integer internId, String internName,
                               Integer mentorId, String mentorName, LocalDateTime assignedAt) {
        this.assignmentId = assignmentId;
        this.internId = internId;
        this.internName = internName;
        this.mentorId = mentorId;
        this.mentorName = mentorName;
        this.assignedAt = assignedAt;
    }

    public Integer getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Integer assignmentId) { this.assignmentId = assignmentId; }

    public Integer getInternId() { return internId; }
    public void setInternId(Integer internId) { this.internId = internId; }

    public String getInternName() { return internName; }
    public void setInternName(String internName) { this.internName = internName; }

    public Integer getMentorId() { return mentorId; }
    public void setMentorId(Integer mentorId) { this.mentorId = mentorId; }

    public String getMentorName() { return mentorName; }
    public void setMentorName(String mentorName) { this.mentorName = mentorName; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
