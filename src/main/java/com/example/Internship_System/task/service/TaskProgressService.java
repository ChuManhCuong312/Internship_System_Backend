package com.example.Internship_System.task.service;

import com.example.Internship_System.repository.TaskProgressRepository;
import com.example.Internship_System.task.dto.TaskProgressDTO;
import com.example.Internship_System.task.entity.TaskProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskProgressService {
    @Autowired
    private TaskProgressRepository repository;

    public TaskProgress save(TaskProgress taskProgress) {
        taskProgress.setUpdatedAt(LocalDateTime.now());
        return repository.save(taskProgress);
    }

    public Optional<TaskProgress> findById(int id) {
        return repository.findById(id);
    }

    public Optional<TaskProgress> findByTaskId(int taskId) {
        return repository.findByTaskId(taskId);
    }

    public List<TaskProgress> findAllByTaskId(int taskId) {
        return repository.findAllByTaskId(taskId);
    }

    public List<TaskProgress> findAll() {
        return repository.findAll();
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    private TaskProgressDTO convertToDTO(TaskProgress taskProgress) {
        return new TaskProgressDTO(
                taskProgress.getProgressId(),
                taskProgress.getTaskId(),
                taskProgress.getPercentComplete(),
                taskProgress.getNote(),
                taskProgress.getUpdatedAt()
        );
    }

    public TaskProgressDTO findByTaskIdWithDetails(int taskId) {
        Optional<TaskProgress> taskProgress = repository.findByTaskId(taskId);
        return taskProgress.map(this::convertToDTO).orElse(null);
    }

    public List<TaskProgressDTO> findAllWithDetails() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }
}
