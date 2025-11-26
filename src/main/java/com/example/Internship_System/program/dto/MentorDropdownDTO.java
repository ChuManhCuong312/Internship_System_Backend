package com.example.Internship_System.program.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


public class MentorDropdownDTO {
    private Integer mentorId;
    private String fullName;

    public MentorDropdownDTO(){}

    public MentorDropdownDTO(Integer mentorId, String fullName){
        this.mentorId = mentorId;
        this.fullName = fullName;
    }

    public Integer getMentorId() {return mentorId;}

    public void setMentorId(Integer mentorId) {this.mentorId = mentorId;}

    public String getFullName() {return fullName;}
    public void setFullName(String fullName) {this.fullName = fullName;}
}
