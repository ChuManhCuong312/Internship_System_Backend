package com.example.Internship_System.intern.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "intern_logs")
public class InternLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "intern_id")
    private int internId;
    
    @Column(name = "details")
    private String details;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public int getInternId() { return internId; }
    public void setInternId(int internId) { this.internId = internId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDetails() { return details; }
    public void setDetails(String message) { this.details = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}

