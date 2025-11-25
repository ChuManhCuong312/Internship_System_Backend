package com.example.Internship_System.team.entity;

import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.program.entity.Program;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer teamId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id", nullable = false)
    private MentorUser mentor;

    @Column(name = "assigned_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime assignedDate = LocalDateTime.now();

    public Team() {}

    public Team(Program program, MentorUser mentor, LocalDateTime assignedDate){
        this.program = program;
        this.mentor = mentor;
        this.assignedDate = assignedDate;
    }

    public Integer getTeamId() {return teamId;}
    public void setTeamId(Integer TeamId) {this.teamId = TeamId;}

    public Program getProgram() {return program;}
    public void setProgram(Program program) {this.program = program;}

    public MentorUser getMentor() {return mentor;}
    public void setMentor(MentorUser mentor) {this.mentor = mentor;}

    public LocalDateTime getAssignedDate() {return assignedDate;}
    public void setAssignedDate(LocalDateTime assignedDate) {this.assignedDate = assignedDate;}

}
