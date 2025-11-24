package com.example.Internship_System.team.dto;

import java.util.List;

public class CreateTeamDTO {
    private Integer programId;
    private Integer mentorId;
    private List<Integer> internIds;

    public CreateTeamDTO(Integer programId, Integer mentorId, List<Integer> internIds){
        this.programId = programId;
        this.mentorId = mentorId;
        this.internIds = internIds;
    }

    public Integer getProgramId() {return programId;}
    public void setProgramId(Integer programId) {this.programId = programId;}

    public Integer getMentorId() {return mentorId;}
    public void setMentorId(Integer mentorId) {this.mentorId = mentorId;}

    public List<Integer> getInternIds() {return internIds;}
    public void setInternIds(List<Integer> internIds) {this.internIds = internIds;}
}
