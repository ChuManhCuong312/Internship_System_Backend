package com.example.Internship_System.team.dto;

public class AssignMentorDTO {
    private Integer programId;
    private Integer mentorId;

    public AssignMentorDTO(Integer programId, Integer mentorId){
        this.programId = programId;
        this.mentorId = mentorId;
    }

    public Integer getMentorId() {return mentorId;}
    public void setMentorId(Integer mentorId) {this.mentorId = mentorId;}

    public Integer getProgramId() {return programId;}
    public void setProgramId(Integer programId) {this.programId = programId;}
}
