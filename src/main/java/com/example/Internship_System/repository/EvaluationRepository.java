package com.example.Internship_System.repository;

import com.example.Internship_System.evaluation.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    // Tùy theo thuộc tính trong Evaluation: nếu là InternProfile intern;
    List<Evaluation> findByIntern_InternId(Integer internId);
    @Query("SELECT COALESCE(SUM(e.weight), 0) FROM Evaluation e WHERE e.intern.internId = :internId")
    Integer sumWeightByInternId(@Param("internId") Integer internId);

    @Query("""
           SELECT COALESCE(SUM(e.weight), 0)
           FROM Evaluation e
           WHERE e.intern.internId = :internId
             AND e.evaluationId <> :evaluationId
           """)
    Integer sumWeightByInternExcludeCurrent(@Param("internId") Integer internId,
                                            @Param("evaluationId") Integer evaluationId);
}