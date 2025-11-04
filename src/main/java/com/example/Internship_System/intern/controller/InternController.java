package com.example.Internship_System.intern.controller;

import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.intern.service.InternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/interns")
@CrossOrigin(origins = "*")
public class InternController {

    @Autowired
    private InternService internService;

    // Create - Add new intern profile
    @PostMapping
    public ResponseEntity<InternProfile> createInternProfile(@RequestBody InternProfile internProfile) {
        try {
            InternProfile savedProfile = internService.save(internProfile);
            return new ResponseEntity<>(savedProfile, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Read - Get all intern profiles
    @GetMapping
    public ResponseEntity<List<InternProfile>> getAllInternProfiles() {
        try {
            List<InternProfile> profiles = internService.findAll();
            if (profiles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(profiles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Read - Get intern profile by ID
    @GetMapping("/{id}")
    public ResponseEntity<InternProfile> getInternProfileById(@PathVariable("id") int id) {
        Optional<InternProfile> profile = internService.findById(id);
        return profile.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Read - Get intern profile by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<InternProfile> getInternProfileByUserId(@PathVariable("userId") int userId) {
        Optional<InternProfile> profile = internService.findByUserId(userId);
        return profile.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Read - Get intern profiles by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InternProfile>> getInternProfilesByStatus(@PathVariable("status") String status) {
        try {
            List<InternProfile> profiles = internService.findByStatus(status);
            if (profiles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(profiles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update - Update intern profile
    @PutMapping("/{id}")
    public ResponseEntity<InternProfile> updateInternProfile(@PathVariable("id") int id,
            @RequestBody InternProfile internProfile) {
        Optional<InternProfile> existingProfile = internService.findById(id);

        if (existingProfile.isPresent()) {
            InternProfile profileToUpdate = existingProfile.get();
            profileToUpdate.setUserId(internProfile.getUserId());
            profileToUpdate.setSchool(internProfile.getSchool());
            profileToUpdate.setMajor(internProfile.getMajor());
            profileToUpdate.setDob(internProfile.getDob());
            profileToUpdate.setAddress(internProfile.getAddress());
            profileToUpdate.setCvPath(internProfile.getCvPath());
            profileToUpdate.setStatus(internProfile.getStatus());

            return new ResponseEntity<>(internService.save(profileToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update - Partial update (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<InternProfile> partialUpdateInternProfile(@PathVariable("id") int id,
            @RequestBody InternProfile internProfile) {
        Optional<InternProfile> existingProfile = internService.findById(id);

        if (existingProfile.isPresent()) {
            InternProfile profileToUpdate = existingProfile.get();

            if (internProfile.getSchool() != null) {
                profileToUpdate.setSchool(internProfile.getSchool());
            }
            if (internProfile.getMajor() != null) {
                profileToUpdate.setMajor(internProfile.getMajor());
            }
            if (internProfile.getDob() != null) {
                profileToUpdate.setDob(internProfile.getDob());
            }
            if (internProfile.getAddress() != null) {
                profileToUpdate.setAddress(internProfile.getAddress());
            }
            if (internProfile.getCvPath() != null) {
                profileToUpdate.setCvPath(internProfile.getCvPath());
            }
            if (internProfile.getStatus() != null) {
                profileToUpdate.setStatus(internProfile.getStatus());
            }

            return new ResponseEntity<>(internService.save(profileToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete - Delete intern profile by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteInternProfile(@PathVariable("id") int id) {
        try {
            Optional<InternProfile> profile = internService.findById(id);
            if (profile.isPresent()) {
                internService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete - Delete all intern profiles
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllInternProfiles() {
        try {
            internService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}