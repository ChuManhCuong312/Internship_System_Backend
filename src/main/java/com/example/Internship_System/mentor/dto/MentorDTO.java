package com.example.Internship_System.mentor.dto;


public class MentorDTO {
    private Integer mentorId;
    private String fullName;
    private String email;

    public MentorDTO(Integer mentorId, String fullName, String email) {
        this.mentorId = mentorId;
        this.fullName = fullName;
        this.email = email;
    }

    public Integer getMentorId() { return mentorId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
}
