package com.example.Internship_System.repository;

import com.example.Internship_System.support.entity.SupportRequest;
import com.example.Internship_System.support.entity.SupportStatus;
import com.example.Internship_System.support.entity.SupportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRequestRepository extends JpaRepository<SupportRequest, Integer> {
    List<SupportRequest> findAllByOrderByRequestDateDesc();
    List<SupportRequest> findByInternIdOrderByRequestDateDesc(Integer internId);

    @Query("SELECT sr FROM SupportRequest sr WHERE (:status IS NULL OR sr.status = :status) " +
            "AND (:type IS NULL OR sr.supportType = :type) " +
            "AND (:internId IS NULL OR sr.internId = :internId) " +
            "ORDER BY sr.requestDate DESC")
    List<SupportRequest> findByCriteria(@Param("status") SupportStatus status,
                                        @Param("type") SupportType type,
                                        @Param("internId") Integer internId);
}
