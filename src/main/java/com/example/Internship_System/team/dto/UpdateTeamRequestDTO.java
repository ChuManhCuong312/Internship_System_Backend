package com.example.Internship_System.team.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTeamRequestDTO {
    private Integer mentorId;

    private List<Integer> internIds;

    public UpdateTeamRequestDTO(Integer mentorId, List<Integer> internIds){
        this.mentorId = mentorId;
        this.internIds = internIds;
    }

    public Integer getMentorId() {return mentorId;}
    public void setMentorId(Integer mentorId) {this.mentorId = mentorId;}

    public List<Integer> getInternIds() {return internIds;}
    public void setInternIds(List<Integer> internIds) {this.internIds = internIds;}
}
