package com.example.Internship_System.repository;

import com.example.Internship_System.task.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskTagRepository extends JpaRepository<TaskTag, Integer> {
    List<TaskTag> findByTaskId(Integer taskId);
    List<TaskTag> findByTagId(Integer tagId);
    Optional<TaskTag> findByTaskIdAndTagId(Integer taskId, Integer tagId);
    void deleteByTaskId(Integer taskId);
    void deleteByTagId(Integer tagId);
    boolean existsByTaskIdAndTagId(Integer taskId, Integer tagId);

    @Query("SELECT tt.tagId FROM TaskTag tt WHERE tt.taskId = :taskId")
    List<Integer> findTagIdsByTaskId(@Param("taskId") Integer taskId);

    @Query("SELECT tt.taskId FROM TaskTag tt WHERE tt.tagId = :tagId")
    List<Integer> findTaskIdsByTagId(@Param("tagId") Integer tagId);
}
