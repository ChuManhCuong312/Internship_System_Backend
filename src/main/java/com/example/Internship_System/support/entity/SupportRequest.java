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
    private SupportStatus status = SupportStatus.OPEN;

    @Column(length = 1000)
    private String response;

    @Column(length = 1000)
    private String rejectionReason;

    // @Column(nullable = false, updatable = false)
    // private LocalDateTime requestDate = LocalDateTime.now();

    @Column(nullable = false)
    private Integer processedBy;

    private LocalDateTime processedDate;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SupportRequestHistory> histories = new ArrayList<>();
}
