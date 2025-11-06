package com.example.Internship_System.intern.service;

import com.example.Internship_System.intern.entity.Intern;
import com.example.Internship_System.repository.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InternService {
    @Autowired
    private InternRepository repository;
    public Intern save(Intern profile) {
        return repository.save(profile);
    }
    public List<Intern> findAll() {
        return repository.findAll();
    }
    public Optional<Intern> findById(int id) {
        return repository.findById(id);
    }
    public Optional<Intern> findByUserId(int userId) {
        return repository.findByUserId(userId);
    }
    public List<Intern> findByStatus(String status) {
        return repository.findByStatus(status);
    }
    public void deleteById(int id) {
        repository.deleteById(id);
    }

}