package com.example.Internship_System.evaluation.DTO;

import jakarta.validation.constraints.*;

public class EvaluationRequest {

    @NotNull
    private Integer internId;

    @NotNull
    private Integer mentorId;

    @NotBlank
    private String title;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private Double technical;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private Double communication;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private Double discipline;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private Double attitude;

    @Min(0) @Max(100)
    private Integer weight;

    private String note;

    // ===== GETTERS =====

    public Integer getInternId() {
        return internId;
    }

    public Integer getMentorId() {
        return mentorId;
    }

    public String getTitle() {
        return title;
    }

    public Double getTechnical() {
        return technical;
    }

    public Double getCommunication() {
        return communication;
    }

    public Double getDiscipline() {
        return discipline;
    }

    public Double getAttitude() {
        return attitude;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getNote() {
        return note;
    }

    // ===== SETTERS =====

    public void setInternId(Integer internId) {
        this.internId = internId;
    }

    public void setMentorId(Integer mentorId) {
        this.mentorId = mentorId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTechnical(Double technical) {
        this.technical = technical;
    }

    public void setCommunication(Double communication) {
        this.communication = communication;
    }

    public void setDiscipline(Double discipline) {
        this.discipline = discipline;
    }

    public void setAttitude(Double attitude) {
        this.attitude = attitude;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
