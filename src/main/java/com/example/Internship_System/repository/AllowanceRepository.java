package com.example.Internship_System.repository;

import com.example.Internship_System.allowance.entity.Allowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AllowanceRepository extends JpaRepository<Allowance, Integer> {
    Optional<Allowance> findByAllowanceId(int allowanceId);
    List<Allowance> findByInternId(int internId);

    @Query("SELECT a FROM Allowance a WHERE " +
            "(:internId IS NULL OR a.internId = :internId) AND " +
            "(:type IS NULL OR a.type = :type) AND " +
            "(:minAmount IS NULL OR a.amount >= :minAmount) AND " +
            "(:maxAmount IS NULL OR a.amount <= :maxAmount) AND " +
            "(:startDate IS NULL OR a.dateApplied >= :startDate) AND " +
            "(:endDate IS NULL OR a.dateApplied <= :endDate)")
    List<Allowance> filterAllowances(
            @Param("internId") Integer internId,
            @Param("type") String type,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
