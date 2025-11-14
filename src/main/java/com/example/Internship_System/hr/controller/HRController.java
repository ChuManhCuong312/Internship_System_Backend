package com.example.Internship_System.hr.controller;

import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.hr.service.HRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/hr/interns")
@CrossOrigin(origins = "*")
public class HRController {

    @Autowired
    private HRService hrService;

    @GetMapping
    public ResponseEntity<Page<HRInternDTO>> getAllInternProfilesForHR(Pageable pageable) {
        Page<HRInternDTO> profiles = hrService.getAllInternsForHR(pageable);
        if (profiles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<HRInternDTO>> searchInternProfiles(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String status,
            Pageable pageable
    ) {
        Page<HRInternDTO> profiles = hrService.searchInterns(searchTerm, major, status, pageable);
        if (profiles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateInternStatus(
            @PathVariable int id,
            @RequestParam String status,
            @RequestParam(required = false) String rejectionReason) {
        hrService.updateStatus(id, status, rejectionReason);
        return ResponseEntity.ok().build();
    }
}


