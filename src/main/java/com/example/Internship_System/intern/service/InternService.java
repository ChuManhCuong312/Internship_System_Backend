package com.example.Internship_System.intern.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.Internship_System.repository.InternRepository;
import com.example.Internship_System.intern.entity.InternProfile;

@Service
@Transactional
public class InternService {

    @Autowired
    private InternRepository internRepository;

    public InternProfile createIntern(InternProfile internProfile) {
        if (internRepository.existsByUserId(internProfile.getUser_id())) {
            throw new RuntimeException("Intern profile already exists for user_id: " + internProfile.getUser_id());
        }
        return internRepository.save(internProfile);
    }

    public List<InternProfile> getAllInterns() {
        return internRepository.findAll();
    }

    public InternProfile getInternById(int internId) {
        return internRepository.findById(internId)
                .orElseThrow(() -> new RuntimeException("Intern not found with id: " + internId));
    }

    public InternProfile getInternByUserId(int userId) {
        return internRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Intern not found with user_id: " + userId));
    }

    public InternProfile updateIntern(int internId, InternProfile internProfile) {
        InternProfile existing = getInternById(internId);

        existing.setSchool(internProfile.getSchool());
        existing.setMajor(internProfile.getMajor());
        existing.setDob(internProfile.getDob());
        existing.setAddress(internProfile.getAddress());
        existing.setCv_path(internProfile.getCv_path());

        if (internProfile.getStatus() != null) {
            existing.setStatus(internProfile.getStatus());
        }

        return internRepository.save(existing);
    }

    public void deleteIntern(int internId) {
        InternProfile intern = getInternById(internId);
        internRepository.delete(intern);
    }

    public InternProfile updateStatus(int internId, String status) {
        InternProfile intern = getInternById(internId);
        intern.setStatus(status);
        return internRepository.save(intern);
    }
}