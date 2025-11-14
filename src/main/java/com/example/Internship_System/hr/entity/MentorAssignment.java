package com.example.Internship_System.hr.entity;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.mentor.entity.MentorProfile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentor_assignments")
public class MentorAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Integer assignmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id",nullable = false)
    private MentorProfile mentor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "intern_id", nullable = false)
    private InternProfile intern;

    @Column(name = "assigned_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime assignedAt = LocalDateTime.now();

    public MentorAssignment() {}

    public MentorAssignment(MentorProfile mentor, InternProfile intern, LocalDateTime assignedAt) {
        this.mentor = mentor;
        this.intern = intern;
        this.assignedAt = assignedAt;
    }

    public Integer getAssignmentId() { return assignmentId;}
    public void setAssignmentId(Integer assignmentId) { this.assignmentId = assignmentId;}

    public MentorProfile getMentor() {return mentor;}
    public void setMentor(MentorProfile mentor) {this.mentor = mentor;}

    public InternProfile getIntern() {return intern;}
    public void setIntern(InternProfile intern) {this.intern = intern;}

    public LocalDateTime getAssignedAt() {return  assignedAt;}
    public void setAssignedAt(LocalDateTime assignedAt) {this.assignedAt = assignedAt;}
}
