
package com.example.Internship_System.evaluation.entity;

import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.mentor.entity.MentorUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Integer evaluationId;

    // Khóa ngoại tới intern_users(intern_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intern_id", referencedColumnName = "intern_id")
    private InternProfile intern;

    // Khóa ngoại tới mentor_users(mentor_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_evaluate_id", referencedColumnName = "mentor_id")
    private MentorUser mentorEvaluate;

    @NotBlank
    @Size(max = 255)
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    // ===== điểm số dùng Double =====
    @DecimalMin("0.0") @DecimalMax("10.0")
    @Column(name = "technical")
    private Double technical;

    @DecimalMin("0.0") @DecimalMax("10.0")
    @Column(name = "communication")
    private Double communication;

    @DecimalMin("0.0") @DecimalMax("10.0")
    @Column(name = "discipline")
    private Double discipline;

    @DecimalMin("0.0") @DecimalMax("10.0")
    @Column(name = "attitude")
    private Double attitude;

    @Min(0) @Max(100)
    @Column(name = "weight")
    private Integer weight;

    @Lob
    @Column(name = "note")
    private String note;

    // Giá trị mặc định do DB set CURRENT_TIMESTAMP; không cần @CreationTimestamp nếu bạn muốn thuần JPA
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    // ===== Constructors =====
    public Evaluation() {}

    public Evaluation(Integer evaluationId, InternProfile intern, MentorUser mentorEvaluate, String title,
                      Double technical, Double communication, Double discipline, Double attitude,
                      Integer weight, String note, LocalDateTime createdAt) {
        this.evaluationId = evaluationId;
        this.intern = intern;
        this.mentorEvaluate = mentorEvaluate;
        this.title = title;
        this.technical = technical;
        this.communication = communication;
        this.discipline = discipline;
        this.attitude = attitude;
        this.weight = weight;
        this.note = note;
        this.createdAt = createdAt;
    }

    // ===== Getters & Setters =====
    public Integer getEvaluationId() { return evaluationId; }
    public void setEvaluationId(Integer evaluationId) { this.evaluationId = evaluationId; }

    public InternProfile getIntern() { return intern; }
    public void setIntern(InternProfile intern) { this.intern = intern; }

    public MentorUser getMentorEvaluate() { return mentorEvaluate; }
    public void setMentorEvaluate(MentorUser mentorEvaluate) { this.mentorEvaluate = mentorEvaluate; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getTechnical() { return technical; }
    public void setTechnical(Double technical) { this.technical = technical; }

    public Double getCommunication() { return communication; }
    public void setCommunication(Double communication) { this.communication = communication; }

    public Double getDiscipline() { return discipline; }
    public void setDiscipline(Double discipline) { this.discipline = discipline; }

    public Double getAttitude() { return attitude; }
    public void setAttitude(Double attitude) { this.attitude = attitude; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
