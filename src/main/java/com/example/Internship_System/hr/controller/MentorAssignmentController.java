package com.example.Internship_System.hr.controller;

import com.example.Internship_System.hr.dto.MentorAssignmentDTO;
import com.example.Internship_System.hr.dto.MentorAssignmentRequestDTO;
import com.example.Internship_System.hr.entity.MentorAssignment;
import com.example.Internship_System.hr.service.MentorAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/mentor-assignments")
public class MentorAssignmentController {

    @Autowired
    private MentorAssignmentService mentorAssignmentService;

    @GetMapping
    public ResponseEntity<List<MentorAssignmentDTO>> getAllAssignments() {
        return ResponseEntity.ok(mentorAssignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorAssignmentDTO> getAssignmentById(@PathVariable Integer id) {
        return mentorAssignmentService.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MentorAssignmentDTO> createAssignment(@RequestBody MentorAssignmentRequestDTO request) {
        return ResponseEntity.ok(mentorAssignmentService.createAssignment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MentorAssignmentDTO> updateAssignment(
            @PathVariable Integer id,
            @RequestBody MentorAssignmentRequestDTO request) {
        return ResponseEntity.ok(mentorAssignmentService.updateAssignment(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer id) {
        mentorAssignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
