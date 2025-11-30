package com.example.Internship_System.repository;

import com.example.Internship_System.task.entity.TaskProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskProgressRepository extends JpaRepository<TaskProgress, Integer> {
    Optional<TaskProgress> findByTaskId(int taskId);
    List<TaskProgress> findAllByTaskId(int taskId);
}
