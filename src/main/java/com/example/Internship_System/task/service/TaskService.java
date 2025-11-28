package com.example.Internship_System.task.service;

import com.example.Internship_System.repository.TaskRepository;
import com.example.Internship_System.task.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Optional<Task> findById(int id) {
        return repository.findById(id);
    }

    public List<Task> findByMentorId(int mentorId) {
        return repository.findByMentorId(mentorId);
    }

    public List<Task> findByProgramId(int programId) {
        return repository.findByProgramId(programId);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }
}
