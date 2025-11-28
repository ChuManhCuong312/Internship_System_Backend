package com.example.Internship_System.task.service;

import com.example.Internship_System.repository.TaskRepository;
import com.example.Internship_System.task.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    public Task save(Task task) {
        return repository.save(task);
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Optional<Task> findTaskByMentorId(int mentorId) {
        return repository.findTaskByMentorId(mentorId);
    }

    public Optional<Task> findTaskByInternId(int mentorId) {
        return repository.findTaskByInternId(mentorId);
    }

    public List<Task> findByInternId(int internId) {
        return repository.findByInternId(internId);
    }

    public Optional<Task> findById(int taskId) {
        return repository.findById(taskId);
    }

    public Task saveAndFlush(Task task) {
        return repository.saveAndFlush(task);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }
}
