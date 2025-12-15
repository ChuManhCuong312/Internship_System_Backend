package com.example.Internship_System.repository;

import com.example.Internship_System.support.dto.SupportRequestDTO;
import com.example.Internship_System.support.entity.SupportRequest;
import com.example.Internship_System.support.entity.SupportStatus;
import com.example.Internship_System.support.entity.SupportType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRequestRepository extends JpaRepository<SupportRequest, Integer> {
        List<SupportRequest> findAllByOrderByProcessedDateDesc();

        List<SupportRequest> findByInternIdOrderByProcessedDateDesc(Integer internId);

        @Query("""
                            SELECT new com.example.Internship_System.support.dto.SupportRequestDTO(
                                sr.supportId,
                                sr.internId,
                                sr.supportType,
                                sr.title,
                                sr.description,
                                sr.status,
                                sr.response,
                                sr.rejectionReason,
                                sr.processedBy,
                                pu.fullName,
                                sr.processedDate,
                                sr.createdAt,
                                iu.fullName
                            )
                            FROM SupportRequest sr
                            JOIN InternProfile i ON sr.internId = i.internId
                            JOIN User iu ON i.userId = iu.userId
                            LEFT JOIN User pu ON sr.processedBy = pu.userId
                            WHERE (:status IS NULL OR sr.status = :status)
                              AND (:type IS NULL OR sr.supportType = :type)
                              AND (:internId IS NULL OR sr.internId = :internId)
                              AND (:keyword IS NULL OR LOWER(iu.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
                            ORDER BY sr.processedDate DESC
                        """)
        Page<SupportRequestDTO> findByCriteria(
                        @Param("status") SupportStatus status,
                        @Param("type") SupportType type,
                        @Param("internId") Integer internId,
                        @Param("keyword") String keyword,
                        Pageable pageable);

}
