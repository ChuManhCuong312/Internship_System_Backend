package com.example.Internship_System.hr.service;

import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.repository.HRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}

