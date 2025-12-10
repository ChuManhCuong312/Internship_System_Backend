package com.example.Internship_System.team.dto;

import java.util.List;

public class CreateAutoTeamsRequest {
    private Integer programId;
    private List<Integer> internIds; // chosen interns from frontend checkboxes
    private Integer numberOfTeams;

    // getters & setters

    public Integer getProgramId() {return programId;}
    public void setProgramId(Integer programId) {this.programId = programId;}

    public List<Integer> getInternIds() {return internIds;}
    public void setInternIds(List<Integer> internIds) {this.internIds = internIds;}

    public Integer getNumberOfTeams() {return numberOfTeams;}
    public void setNumberOfTeams(Integer numberOfTeams) {this.numberOfTeams = numberOfTeams;}
}