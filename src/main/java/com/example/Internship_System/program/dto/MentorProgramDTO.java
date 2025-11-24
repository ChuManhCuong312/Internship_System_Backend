package com.example.Internship_System.program.dto;

public class MentorProgramDTO {
    private Integer programId;
    private String programName;
    private String mentorName;

    public MentorProgramDTO(Integer programId, String programName, String mentorName) {
        this.programId = programId;
        this.programName = programName;
        this.mentorName = mentorName;
    }

    public Integer getProgramId() {return programId;}
    public String getProgramName() {return programName;}
    public String getMentorName() {return mentorName;}
}
