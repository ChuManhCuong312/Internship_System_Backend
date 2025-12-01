package com.example.Internship_System.team.dto;

public class TeamSimpleDTO {
    private Integer team_id;
    private String team_name;
    private Integer member_count;

    public Integer getTeam_id() { return team_id; }
    public void setTeam_id(Integer team_id) { this.team_id = team_id; }

    public String getTeam_name() { return team_name; }
    public void setTeam_name(String team_name) { this.team_name = team_name; }

    public Integer getMember_count() { return member_count; }
    public void setMember_count(Integer member_count) { this.member_count = member_count; }
}
