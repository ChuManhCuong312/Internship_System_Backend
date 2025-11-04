package com.example.Internship_System.intern.controller;

import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.intern.service.InternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/interns")
@CrossOrigin(origins = "*")
public class InternController {

    @Autowired
    private InternService internProfileService;

    // Create new intern profile
    @PostMapping
    public ResponseEntity<InternProfile> createIntern(@Valid @RequestBody InternProfile internProfile) {
        InternProfile created = internProfileService.createIntern(internProfile);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Get all intern profiles
    @GetMapping
    public ResponseEntity<List<InternProfile>> getAllInterns() {
        List<InternProfile> interns = internProfileService.getAllInterns();
        return new ResponseEntity<>(interns, HttpStatus.OK);
    }

    // Get intern profile by ID
    @GetMapping("/{id}")
    public ResponseEntity<InternProfile> getInternById(@PathVariable("id") int internId) {
        InternProfile intern = internProfileService.getInternById(internId);
        return new ResponseEntity<>(intern, HttpStatus.OK);
    }

    // Get intern profile by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<InternProfile> getInternByUserId(@PathVariable("userId") int userId) {
        InternProfile intern = internProfileService.getInternByUserId(userId);
        return new ResponseEntity<>(intern, HttpStatus.OK);
    }

    // Update intern profile
    @PutMapping("/{id}")
    public ResponseEntity<InternProfile> updateIntern(
            @PathVariable("id") int internId,
            @Valid @RequestBody InternProfile internProfile) {
        InternProfile updated = internProfileService.updateIntern(internId, internProfile);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Delete intern profile
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntern(@PathVariable("id") int internId) {
        internProfileService.deleteIntern(internId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Update intern status
    @PatchMapping("/{id}/status")
    public ResponseEntity<InternProfile> updateInternStatus(
            @PathVariable("id") int internId,
            @RequestParam String status) {
        InternProfile updated = internProfileService.updateStatus(internId, status);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}