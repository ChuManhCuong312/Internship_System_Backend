package com.example.Internship_System.hr.controller;

import com.example.Internship_System.hr.dto.CandidateDTO;
import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.hr.dto.InternUpdateDTO;
import com.example.Internship_System.hr.service.HRService;
import com.example.Internship_System.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.intern.entity.InternProfile;

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

    @GetMapping("/candidates")
    public ResponseEntity<Page<CandidateDTO>> getInternCandidatesWithoutProfile(Pageable pageable) {
        Page<CandidateDTO> candidates = hrService.getInternCandidatesWithoutProfile(pageable);
        if (candidates.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(candidates, HttpStatus.OK);
    }

    @PostMapping("/{userId}/profile")
    public ResponseEntity<?> createInternProfile(
            @PathVariable int userId,
            @RequestParam(required = false) String phone,
            @ModelAttribute InternProfile profileData) {
        try {
            hrService.createInternProfileForUser(userId, phone, profileData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PatchMapping("/{id}/profile")
    public ResponseEntity<?> updateInternProfile(
            @PathVariable int id,
            @Valid @RequestBody InternUpdateDTO dto) {
        try {
            hrService.updateInternProfile(id, dto);
            return ResponseEntity.ok().build();
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            String msg = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
            if (msg.contains("phone")) {
                return ResponseEntity.badRequest().body("Số điện thoại đã tồn tại trong hệ thống");
            } else if (msg.contains("email")) {
                return ResponseEntity.badRequest().body("Email đã tồn tại trong hệ thống");
            }
            return ResponseEntity.badRequest().body("Dữ liệu bị trùng hoặc vi phạm ràng buộc");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().iterator().next().getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
}


