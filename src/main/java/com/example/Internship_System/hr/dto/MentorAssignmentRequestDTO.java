package com.example.Internship_System.hr.dto;

import jakarta.validation.constraints.NotNull;

public class MentorAssignmentRequestDTO {

    @NotNull(message = "Intern ID is required")
    private Integer internId;

    @NotNull(message = "Mentor ID is required")
    private Integer mentorId;

    public MentorAssignmentRequestDTO() {}

    public MentorAssignmentRequestDTO(Integer internId, Integer mentorId) {
        this.internId = internId;
        this.mentorId = mentorId;
    }

    public Integer getInternId() { return internId; }
    public void setInternId(Integer internId) { this.internId = internId; }

    public Integer getMentorId() { return mentorId; }
    public void setMentorId(Integer mentorId) { this.mentorId = mentorId; }
}

