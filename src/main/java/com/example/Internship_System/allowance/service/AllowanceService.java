package com.example.Internship_System.allowance.service;

import com.example.Internship_System.allowance.entity.Allowance;
import com.example.Internship_System.repository.AllowanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AllowanceService {
    @Autowired
    private AllowanceRepository repository;

    public Allowance save(Allowance allowance) {
        return repository.save(allowance);
    }

    public List<Allowance> findAll() {
        return repository.findAll();
    }

    public Optional<Allowance> findById(int id) {
        return repository.findById(id);
    }

    public List<Allowance> findByInternId(int internId) {
        return repository.findByInternId(internId);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }
}
