package com.example.Internship_System.team.dto;

import java.util.List;

public class ProgramOverviewDTO {
    private long totalTeams;
    private long totalInterns;
    private long totalMentors;
    private List<String> mentorNames;

    public ProgramOverviewDTO(long totalTeams, long totalInterns, long totalMentors, List<String> mentorNames) {
        this.totalTeams = totalTeams;
        this.totalInterns = totalInterns;
        this.totalMentors = totalMentors;
        this.mentorNames = mentorNames;
    }

    // GETTERS & SETTERS
    public long getTotalTeams() {return totalTeams;}
    public void setTotalTeams(long totalTeams) {this.totalTeams = totalTeams;}

    public long getTotalInterns() {return totalInterns;}
    public void setTotalInterns(long totalInterns) {this.totalInterns = totalInterns;}

    public long getTotalMentors() {return totalMentors;}
    public void setTotalMentors(long totalMentors) {this.totalMentors = totalMentors;}

    public List<String> getMentorNames() {return mentorNames;}
    public void setMentorNames(List<String> mentorNames) {this.mentorNames = mentorNames;}
}
