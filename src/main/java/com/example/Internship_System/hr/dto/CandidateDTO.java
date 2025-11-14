package com.example.Internship_System.hr.dto;

public class CandidateDTO {
    private Integer userId;
    private String fullName;
    private String email;
    private String phone;

    public CandidateDTO(Integer userId, String fullName, String email, String phone) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
