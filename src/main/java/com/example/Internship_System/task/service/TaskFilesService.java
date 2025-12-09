package com.example.Internship_System.task.service;

import com.example.Internship_System.repository.TaskFilesRepository;
import com.example.Internship_System.task.dto.TaskFilesDTO;
import com.example.Internship_System.task.entity.TaskFiles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskFilesService {
    private final TaskFilesRepository repository;

    public TaskFiles save(TaskFiles taskFiles) {
        return repository.save(taskFiles);
    }

    public Optional<TaskFiles> findById(int id) {
        return repository.findById(id);
    }
@SuppressWarnings("unused")
    public List<TaskFiles> findByTaskId(int taskId) {
        return repository.findByTaskId(taskId);
    }

    public List<TaskFiles> findAll() {
        return repository.findAll();
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteByTaskId(int taskId) {
        repository.deleteByTaskId(taskId);
    }

    private TaskFilesDTO convertToDTO(TaskFiles taskFiles) {
        return new TaskFilesDTO(
                taskFiles.getTaskFilesId(),
                taskFiles.getTaskId(),
                taskFiles.getLinkFile()
        );
    }

    public List<TaskFilesDTO> findByTaskIdWithDetails(int taskId) {
        return repository.findByTaskId(taskId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TaskFilesDTO> findAllWithDetails() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }
}
