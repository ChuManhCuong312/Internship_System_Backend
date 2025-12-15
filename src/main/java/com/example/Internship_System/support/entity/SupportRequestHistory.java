package com.example.Internship_System.support.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "support_request_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportRequestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_id", nullable = false)
    @JsonIgnore // tránh lỗi ByteBuddy proxy
    private SupportRequest request;

    @Enumerated(EnumType.STRING)
    private SupportStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private SupportStatus newStatus;

    @Column(nullable = false)
    private LocalDateTime changeDate = LocalDateTime.now();

    private Integer changedBy;

    @Column(length = 1000)
    private String remarks;
}
