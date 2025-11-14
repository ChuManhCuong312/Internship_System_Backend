package com.example.Internship_System.hr.service;

import com.example.Internship_System.hr.dto.CandidateDTO;
import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.repository.HRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.auth.entity.User;

import java.util.Optional;

@Service
public class HRService {

    @Autowired
    private HRRepository repository;

    public Page<HRInternDTO> getAllInternsForHR(Pageable pageable) {
        return repository.findAllInternProfilesForHR(null, null, null, pageable);
    }

    public Page<HRInternDTO> searchInterns(String searchTerm, String major, String status, Pageable pageable) {
        return repository.findAllInternProfilesForHR(searchTerm, major, status, pageable);
    }

    public void updateStatus(int internId, String status, String rejectionReason) {
        InternProfile intern = repository.findById(internId)
                .orElseThrow(() -> new RuntimeException("Intern profile not found with id: " + internId));

        intern.setStatus(status);

        if ("REJECTED".equalsIgnoreCase(status)) {
            intern.setRejectionReason(rejectionReason);
        } else {
            intern.setRejectionReason(null);
        }

        repository.save(intern);
    }
    public void createInternProfileForUser(User user, InternProfile profileData) {
        profileData.setUserId(user.getUserId());
        profileData.setStatus("NO_FILE");
        repository.save(profileData);
    }

    public Page<CandidateDTO> getInternCandidatesWithoutProfile(Pageable pageable) {
        return repository.findInternUsersWithoutProfile(pageable);
    }
}


