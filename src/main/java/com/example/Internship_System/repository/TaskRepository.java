package com.example.Internship_System.repository;

import com.example.Internship_System.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByTaskId(int taskId);
    List<Task> findByMentorId(int mentorId);
    List<Task> findByProgramId(int programId);
    List<Task> findByTaskIdIn(List<Integer> ids); // task của team

    @Query("SELECT t FROM Task t WHERE " +
            "(:mentorId IS NULL OR t.mentorId = :mentorId) AND " +
            "(:programId IS NULL OR t.programId = :programId) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:startDate IS NULL OR t.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR t.createdAt <= :endDate)")
    List<Task> filterTasks(
            @Param("mentorId") Integer mentorId,
            @Param("programId") Integer programId,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
