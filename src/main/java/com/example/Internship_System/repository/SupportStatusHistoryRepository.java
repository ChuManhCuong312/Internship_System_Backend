package com.example.Internship_System.repository;

import com.example.Internship_System.support.entity.SupportStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportStatusHistoryRepository extends JpaRepository<SupportStatusHistory, Integer> {
    List<SupportStatusHistory> findBySupportIdOrderByChangeDateDesc(Integer supportId);
}

