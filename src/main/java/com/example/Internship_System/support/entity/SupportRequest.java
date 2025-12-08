package com.example.Internship_System.support.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer supportId;

    @Column(nullable = false)
    private Integer internId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportType supportType = SupportType.OTHER;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportStatus status = SupportStatus.PENDING;

    @Column(length = 1000)
    private String response;

    @Column(length = 1000)
    private String rejectionReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime requestDate = LocalDateTime.now();

    private Integer processedBy;

    private LocalDateTime processedDate;
}
