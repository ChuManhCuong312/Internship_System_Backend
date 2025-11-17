package com.example.Internship_System.hr.controller;


import com.example.Internship_System.hr.dto.InternAssignmentViewDTO;
import com.example.Internship_System.hr.dto.MentorAssignmentDTO;
import com.example.Internship_System.hr.dto.MentorAssignmentRequestDTO;
import com.example.Internship_System.hr.entity.MentorAssignment;
import com.example.Internship_System.hr.mapper.MentorAssignmentMapper;
import com.example.Internship_System.hr.service.MentorAssignmentService;
import com.example.Internship_System.intern.entity.InternProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hr/mentor-assignments")
public class MentorAssignmentController {
    private final MentorAssignmentService mentorAssignmentService;

    public MentorAssignmentController(MentorAssignmentService mentorAssignmentService) {
        this.mentorAssignmentService = mentorAssignmentService;
    }


    @GetMapping
    public ResponseEntity<List<MentorAssignmentDTO>> getAllAssignments() {
        List<MentorAssignment> assignments = mentorAssignmentService.getAllAssignments();
        List<MentorAssignmentDTO> dtos = assignments.stream()
                .map(MentorAssignmentMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/no-mentor")
    public ResponseEntity<List<InternProfile>> getInternsWithoutMentor() {
        List<InternProfile> interns = mentorAssignmentService.getInternsWithoutMentor();
        return ResponseEntity.ok(interns);
    }

    @PostMapping("/assign")
    public ResponseEntity<MentorAssignmentDTO> assignMentor(
            @Validated @RequestBody MentorAssignmentRequestDTO requestDTO
    ) {
        MentorAssignment assignment = mentorAssignmentService.assignMentor(
                requestDTO.getInternId(),
                requestDTO.getMentorId()
        );
        return ResponseEntity.ok(MentorAssignmentMapper.toDTO(assignment));
    }

    @PutMapping("/reassign")
    public ResponseEntity<MentorAssignmentDTO> reassignMentor(
            @Validated @RequestBody MentorAssignmentRequestDTO requestDTO
    ) {
        MentorAssignment updated = mentorAssignmentService.reassignMentor(
                requestDTO.getInternId(),
                requestDTO.getMentorId()
        );
        return ResponseEntity.ok(MentorAssignmentMapper.toDTO(updated));
    }

    @GetMapping("/interns")
    public ResponseEntity<List<InternAssignmentViewDTO>> getInternsWithAssignments(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "filter", required = false, defaultValue = "all") String filter
    ) {
        List<InternAssignmentViewDTO> list = mentorAssignmentService.listInternsWithAssignments(search, filter);
        return ResponseEntity.ok(list);
    }
}
