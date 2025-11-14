package com.example.Internship_System.intern.service;
import com.example.Internship_System.intern.dto.InternProfileDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.repository.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.intern.mapper.InternProfileMapper;
import com.example.Internship_System.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InternService {

    @Autowired
    private InternRepository repository;

    @Autowired
    private UserRepository userRepository;

    public InternProfileDTO toDTO(InternProfile intern) {
        User user = userRepository.findById(intern.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return InternProfileMapper.toDTO(intern, user);
    }

    public List<InternProfileDTO> getAllInterns() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public InternProfile save(InternProfile profile) {
        return repository.save(profile);
    }

    public Optional<InternProfile> findById(int id) {
        return repository.findById(id);
    }

    public Optional<InternProfile> findByUserId(int userId) {
        return repository.findByUser_UserId(userId);
    }

    public List<InternProfile> findByStatus(String status) {
        return repository.findByStatus(status);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public List<InternProfileDTO> searchInterns(String searchTerm, String major, String status) {
        String search = (searchTerm != null && !searchTerm.trim().isEmpty()) ? searchTerm.trim() : null;
        String majorFilter = (major != null && !major.trim().isEmpty()) ? major.trim() : null;
        String statusFilter = (status != null && !status.trim().isEmpty()) ? status.trim() : null;

        return repository.searchInterns(search, majorFilter, statusFilter);
    }

    public List<String> getDistinctMajors() {
        return repository.findDistinctMajors();
    }
}
