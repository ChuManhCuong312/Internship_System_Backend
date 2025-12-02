package com.example.Internship_System.repository;

import com.example.Internship_System.support.entity.SupportRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRequestHistoryRepository extends JpaRepository<SupportRequestHistory, Integer> {
    List<SupportRequestHistory> findByRequest_SupportIdOrderByChangeDateAsc(Integer supportId);
}
