package com.example.Internship_System.team.entity;

import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.program.entity.Program;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_intern")
public class TeamIntern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_intern_id")
    private Integer teamInternId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "intern_id", nullable = false)
    private InternProfile intern;

    @Column(name = "assigned_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime assignedDate = LocalDateTime.now();

    public TeamIntern() {}

    public TeamIntern(Team team, InternProfile intern, LocalDateTime assignedDate){
        this.team = team;
        this.intern = intern;
        this.assignedDate = assignedDate;
    }

    public Integer getTeamInternId() {return teamInternId;}
    public void setTeamInternId(Integer teamInternId) {this.teamInternId = teamInternId;}

    public Team getTeam() {return team;}
    public void setTeam(Team team) {this.team = team;}

    public InternProfile getIntern() {return intern;}
    public void setIntern(InternProfile intern) {this.intern = intern;}

    public LocalDateTime getAssignedDate() {return assignedDate;}

    public void setAssignedDate(LocalDateTime assignedDate) {this.assignedDate = assignedDate;}
}
