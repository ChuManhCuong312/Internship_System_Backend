package com.example.Internship_System.repository;

import com.example.Internship_System.allowance.entity.Allowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AllowanceRepository extends JpaRepository<Allowance, Integer> {
    Optional<Allowance> findByAllowanceId(int allowanceId);
    List<Allowance> findByInternId(int internId);
}
