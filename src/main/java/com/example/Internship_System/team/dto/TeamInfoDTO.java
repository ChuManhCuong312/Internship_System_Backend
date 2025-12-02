package com.example.Internship_System.team.dto;

import java.util.List;

public class TeamInfoDTO {
    private Integer teamId;
    private Integer mentorId;
    private String mentorName;
    private List<TeamInternInfoDTO> interns;

    public TeamInfoDTO(Integer teamId, Integer mentorId,String mentorName, List<TeamInternInfoDTO> interns) {
        this.teamId = teamId;
        this.mentorId = mentorId;
        this.mentorName = mentorName;
        this.interns = interns;
    }

    public Integer getTeamId() {return teamId;}
    public void setTeamId(Integer teamId) {this.teamId = teamId;}

    public Integer getMentorId() {return mentorId;}
    public void setMentorId(Integer mentorId) {this.mentorId = mentorId;}

    public String getMentorName() {return mentorName;}
    public void setMentorName(String mentorName) {this.mentorName = mentorName;}

    public List<TeamInternInfoDTO> getInterns() {return interns;}
    public void setInterns(List<TeamInternInfoDTO> interns) {this.interns = interns;}
}
