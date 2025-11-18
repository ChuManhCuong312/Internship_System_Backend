package com.example.Internship_System.hr.dto;

public class MentorViewDTO {
    private Integer mentorId;
    private String mentorName;
    private Integer assignedCount;

    public MentorViewDTO(Integer mentorId, String mentorName, Integer assignedCount){
        this.mentorId = mentorId;
        this.mentorName = mentorName;
        this.assignedCount = assignedCount;
    }

    public Integer getMentorId() {return mentorId;}
    public void setMentorId(Integer mentorId) {this.mentorId = mentorId;}

    public String getMentorName() {return mentorName;}
    public void setMentorName(String mentorName) {this.mentorName = mentorName;}

    public Integer getAssignedCount() {return assignedCount;}
    public void setAssignedCount(Integer assignedCount) {this.assignedCount = assignedCount;}
}
