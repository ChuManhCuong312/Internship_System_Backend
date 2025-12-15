package com.example.Internship_System.support.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "support_requests")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "support_id")
    private Integer supportId;

    @Column(name = "intern_id", nullable = false)
    private Integer internId;

    @Enumerated(EnumType.STRING)
    @Column(name = "support_type", nullable = false)
    @Builder.Default
    private SupportType supportType = SupportType.OTHER;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SupportStatus status = SupportStatus.PENDING;

    @Column(length = 1000)
    private String response;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    @Column(name = "request_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime requestDate;

    @Column(name = "processed_by")
    private Integer processedBy;

    @Column(name = "processed_date")
    private LocalDateTime processedDate;

    @PrePersist
    protected void onCreate() {
        if (this.requestDate == null) {
            this.requestDate = LocalDateTime.now();
        }
    }
}
