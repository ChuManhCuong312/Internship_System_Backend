package com.example.Internship_System.intern.service;

import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.repository.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternService {

    @Autowired
    private InternRepository repository;

    public InternProfile save(InternProfile profile) {
        return repository.save(profile);
    }

    public List<InternProfile> findAll() {
        return repository.findAll();
    }

    public Optional<InternProfile> findById(int id) {
        return repository.findById(id);
    }

    public Optional<InternProfile> findByUserId(int userId) {
        return repository.findByUserId(userId);
    }

    public List<InternProfile> findByStatus(String status) {
        return repository.findByStatus(status);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public List<InternProfile> findByMajor(String major) {
        return repository.findByMajorContainingIgnoreCase(major);
    }

    public List<InternProfile> searchInterns(String searchTerm, String major, String status) {
        // Chuyển empty string thành null để query hiệu quả hơn
        String search = (searchTerm != null && !searchTerm.trim().isEmpty())
                ? searchTerm.trim() : null;
        String majorFilter = (major != null && !major.trim().isEmpty())
                ? major.trim() : null;
        String statusFilter = (status != null && !status.trim().isEmpty())
                ? status.trim() : null;

        return repository.searchInterns(search, majorFilter, statusFilter);
    }

    public List<String> getDistinctMajors() {
        return repository.findDistinctMajors();
    }
}