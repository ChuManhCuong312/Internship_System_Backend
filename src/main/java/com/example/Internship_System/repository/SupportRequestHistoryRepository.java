package com.example.Internship_System.repository;

import com.example.Internship_System.support.dto.SupportRequestHistoryDTO;
import com.example.Internship_System.support.entity.SupportRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRequestHistoryRepository extends JpaRepository<SupportRequestHistory, Integer> {
    @Query("""
                SELECT new com.example.Internship_System.support.dto.SupportRequestHistoryDTO(
                    h.historyId,
                    h.oldStatus,
                    h.newStatus,
                    h.changeDate,
                    h.changedBy,
                    u.fullName,
                    h.remarks
                )
                FROM SupportRequestHistory h
                LEFT JOIN User u ON h.changedBy = u.userId
                WHERE h.request.supportId = :supportId
                ORDER BY h.changeDate ASC
            """)
    List<SupportRequestHistoryDTO> findHistoryBySupportId(
            @Param("supportId") Integer supportId);
}
