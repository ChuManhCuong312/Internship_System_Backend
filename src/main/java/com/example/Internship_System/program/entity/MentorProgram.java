package com.example.Internship_System.program.entity;

import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.mentor.entity.MentorUser;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentor_program")
public class MentorProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_program_id")
    private Integer mentorProgramId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id", nullable = false)
    private MentorUser mentor;

    @Column(name = "assigned_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime assignedDate = LocalDateTime.now();

    public MentorProgram() {}

    public MentorProgram(Program program, MentorUser mentor, LocalDateTime assignedDate){
        this.program = program;
        this.mentor = mentor;
        this.assignedDate = assignedDate;
    }

    public Integer getMentorProgramId() {return mentorProgramId;}
    public void setMentorProgramId(Integer mentorProgramId) {this.mentorProgramId = mentorProgramId;}

    public Program getProgram() {return program;}
    public void setProgram(Program program) {this.program = program;}

    public MentorUser getMentor() {return mentor;}
    public void setMentor(MentorUser mentor) {this.mentor = mentor;}

    public LocalDateTime getAssignedDate() {return assignedDate;}
    public void setAssignedDate(LocalDateTime assignedDate) {this.assignedDate = assignedDate;}
}
