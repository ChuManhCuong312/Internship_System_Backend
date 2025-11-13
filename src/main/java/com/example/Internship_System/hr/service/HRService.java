package com.example.Internship_System.hr.service;

import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.repository.HRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HRService {

    @Autowired
    private HRRepository repository;

    public List<HRInternDTO> getAllInternsForHR() {
        return repository.findAllInternProfilesForHR();
    }

    public List<HRInternDTO> searchInterns(String searchTerm, String major, String status) {
        return repository.searchInternProfilesForHR(searchTerm, major, status);
    }
}
