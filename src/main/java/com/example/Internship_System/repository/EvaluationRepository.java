package com.example.Internship_System.repository;

import com.example.Internship_System.evaluation.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    // Tùy theo thuộc tính trong Evaluation: nếu là InternProfile intern;
    List<Evaluation> findByIntern_InternId(Integer internId);

}