package com.example.Internship_System.intern.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leaveId;

    @Column(nullable = false)
    private Integer internId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String reason;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status = LeaveStatus.PENDING;

    private String rejectionReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime requestDate = LocalDateTime.now();

    private Integer processedBy;
}
