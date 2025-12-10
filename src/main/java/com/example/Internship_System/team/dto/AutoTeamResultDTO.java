package com.example.Internship_System.team.dto;

import java.util.List;

public class AutoTeamResultDTO {
    private Integer teamId;
    private List<Integer> internIds;

    public AutoTeamResultDTO(Integer teamId, List<Integer> internIds) {
        this.teamId = teamId;
        this.internIds = internIds;
    }
    // getters/setters

    public Integer getTeamId() {return teamId;}
    public void setTeamId(Integer teamId) {this.teamId = teamId;}

    public List<Integer> getInternIds() {return internIds;}
    public void setInternIds(List<Integer> internIds) {this.internIds = internIds;}
}