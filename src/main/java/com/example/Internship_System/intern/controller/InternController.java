package com.example.Internship_System.intern.controller;

import com.example.Internship_System.intern.dto.InternProfileDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.intern.service.InternService;
import jakarta.validation.Valid;
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

    @GetMapping("/search")
    public ResponseEntity<List<InternProfileDTO>> searchInterns(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String status) {
        try {
            List<InternProfileDTO> results = internService.searchInterns(
                    searchTerm, major, status);

            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/majors")
    public ResponseEntity<List<String>> getMajors() {
        try {
            List<String> majors = internService.getDistinctMajors();
            return new ResponseEntity<>(majors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<InternProfile> createInternProfile(
            @Valid @RequestBody InternProfile internProfile) {
        try {
            InternProfile savedProfile = internService.save(internProfile);
            return new ResponseEntity<>(savedProfile, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<InternProfileDTO>> getAllInternProfiles() {
        try {
            List<InternProfileDTO> profiles = internService.getAllInterns();
            if (profiles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(profiles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternProfile> getInternProfileById(@PathVariable("id") int id) {
        Optional<InternProfile> profile = internService.findById(id);
        return profile.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<InternProfile> getInternProfileByUserId(
            @PathVariable("userId") int userId) {
        Optional<InternProfile> profile = internService.findByUserId(userId);
        return profile.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InternProfile>> getInternProfilesByStatus(
            @PathVariable("status") String status) {
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

    @PutMapping("/{id}")
    public ResponseEntity<InternProfile> updateInternProfile(
            @PathVariable("id") int id,
            @Valid @RequestBody InternProfile internProfile) {
        Optional<InternProfile> existingProfile = internService.findById(id);

        if (existingProfile.isPresent()) {
            InternProfile profileToUpdate = existingProfile.get();
            profileToUpdate.setUserId(internProfile.getUserId());
            profileToUpdate.setSchool(internProfile.getSchool());
            profileToUpdate.setMajor(internProfile.getMajor());
            profileToUpdate.setDob(internProfile.getDob());
            profileToUpdate.setAddress(internProfile.getAddress());

            profileToUpdate.setStatus(internProfile.getStatus());
            profileToUpdate.setGpa(internProfile.getGpa());
            profileToUpdate.setGender(internProfile.getGender());

            profileToUpdate.setRejectionReason(internProfile.getRejectionReason());

            return new ResponseEntity<>(internService.save(profileToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InternProfile> partialUpdateInternProfile(
            @PathVariable("id") int id,
            @Valid @RequestBody InternProfile internProfile) {
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


            if (internProfile.getStatus() != null) {
                profileToUpdate.setStatus(internProfile.getStatus());
            }
            if(internProfile.getGender() != null){
                profileToUpdate.setGender(internProfile.getGender());
            }
            if(internProfile.getGpa() != 0.0){
                profileToUpdate.setGpa(internProfile.getGpa());
            }

            if(internProfile.getRejectionReason() != null){
                profileToUpdate.setRejectionReason(internProfile.getRejectionReason());
            }


            return new ResponseEntity<>(internService.save(profileToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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

}