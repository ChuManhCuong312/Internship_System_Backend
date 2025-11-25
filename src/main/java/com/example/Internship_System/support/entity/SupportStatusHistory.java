package com.example.Internship_System.support.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;

    @Column(nullable = false)
    private Integer supportId;

    @Enumerated(EnumType.STRING)
    private SupportStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportStatus newStatus;

    @Column(nullable = false)
    private Integer changedBy;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime changeDate = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String remarks;
}
