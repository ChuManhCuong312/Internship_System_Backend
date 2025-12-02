package com.example.Internship_System.repository;

import com.example.Internship_System.task.entity.TaskTeamAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskTeamAssignmentRepository extends JpaRepository<TaskTeamAssignment, Integer> {

    @Query("SELECT t.taskId FROM TaskTeamAssignment t WHERE t.teamId = :teamId")
    List<Integer> findTaskIdsByTeamId(Integer teamId);

    List<TaskTeamAssignment> findByTaskId(int taskId);

    List<TaskTeamAssignment> findByTeamId(int teamId);

    void deleteByTaskId(int taskId);
}

