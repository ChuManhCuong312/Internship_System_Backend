package com.example.Internship_System.repository;

import com.example.Internship_System.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findTaskByMentorId(int mentorId);
    List<Task> findByTaskIdIn(List<Integer> ids); // task của team
    List<Task> findByMentorId(int mentorId);
    List<Task> findByProgramId(int programId);

}