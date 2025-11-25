package com.example.Internship_System.allowance.dto;

public class InternSearchDTO {
    private int internId;
    private String fullName;

    public InternSearchDTO() {
    }

    public InternSearchDTO(int internId, String fullName) {
        this.internId = internId;
        this.fullName = fullName;
    }

    public int getInternId() {
        return internId;
    }

    public void setInternId(int internId) {
        this.internId = internId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
