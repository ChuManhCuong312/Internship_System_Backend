package com.example.Internship_System.repository;

import com.example.Internship_System.task.entity.TaskFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskFilesRepository extends JpaRepository<TaskFiles, Integer> {
    List<TaskFiles> findByTaskId(int taskId);
    void deleteByTaskId(int taskId);
}
