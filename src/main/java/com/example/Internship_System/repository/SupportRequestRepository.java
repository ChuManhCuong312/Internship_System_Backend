package com.example.Internship_System.repository;

import com.example.Internship_System.support.entity.SupportRequest;
import com.example.Internship_System.support.entity.SupportStatus;
import com.example.Internship_System.support.entity.SupportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRequestRepository extends JpaRepository<SupportRequest, Integer> {
    List<SupportRequest> findByInternId(Integer internId);
    List<SupportRequest> findByStatus(SupportStatus status);
    List<SupportRequest> findBySupportType(SupportType type);
    List<SupportRequest> findByInternIdAndStatus(Integer internId, SupportStatus status);
    List<SupportRequest> findByInternIdAndSupportType(Integer internId, SupportType type);
    List<SupportRequest> findByStatusAndSupportType(SupportStatus status, SupportType type);
    List<SupportRequest> findByInternIdAndStatusAndSupportType(Integer internId, SupportStatus status, SupportType type);
}

