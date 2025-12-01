package com.example.Internship_System.team.dto;


import com.example.Internship_System.team.entity.Team;

import java.util.List;

public class TeamResponseDTO {
    private Integer teamId;
    private Integer mentorId;
    private String mentorName;

    public TeamResponseDTO(Team team) {
        this.teamId = team.getTeamId();
        this.mentorId = team.getMentor().getMentorId();
        this.mentorName = team.getMentor().getUser().getFullName();
    }

    // getters only
    public Integer getTeamId() { return teamId; }
    public Integer getMentorId() { return mentorId; }
    public String getMentorName() { return mentorName; }

}
