package com.example.Internship_System.repository;

import com.example.Internship_System.intern.entity.LeaveRequest;
import com.example.Internship_System.intern.entity.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByInternId(Integer internId);
    List<LeaveRequest> findByInternIdOrderByRequestDateDesc(Integer internId);
    List<LeaveRequest> findByStatus(LeaveStatus status);
    List<LeaveRequest> findByStatusOrderByRequestDateAsc(LeaveStatus status);

    // Kiểm tra đơn trùng lặp
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.internId = :internId " +
            "AND lr.status = :status " +
            "AND ((lr.startDate BETWEEN :startDate AND :endDate) " +
            "OR (lr.endDate BETWEEN :startDate AND :endDate) " +
            "OR (lr.startDate <= :startDate AND lr.endDate >= :endDate))")
    List<LeaveRequest> findOverlappingRequests(
            @Param("internId") Integer internId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") LeaveStatus status
    );
}