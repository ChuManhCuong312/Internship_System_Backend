package com.example.Internship_System.repository;

import com.example.Internship_System.intern.entity.InternLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternLogRepository extends JpaRepository<InternLog, Long> {
    List<InternLog> findByInternIdOrderByCreatedAtDesc(int internId);
}

