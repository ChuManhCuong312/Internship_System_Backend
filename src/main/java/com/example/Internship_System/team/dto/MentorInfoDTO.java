package com.example.Internship_System.team.dto;

public class MentorInfoDTO {
    private Integer mentorId;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private String expertise;

    public MentorInfoDTO(Integer mentorId, String fullName, String email, String phone, String department, String expertise) {
        this.mentorId = mentorId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.expertise = expertise;
    }

    public Integer getMentorId() { return mentorId; }
    public void setMentorId(Integer mentorId) { this.mentorId = mentorId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getExpertise() { return expertise; }
    public void setExpertise(String expertise) { this.expertise = expertise; }
}
