package com.example.Internship_System.program.controller;

import com.example.Internship_System.program.dto.MentorProgramDTO;
import com.example.Internship_System.program.dto.ProgramCloneDTO;
import com.example.Internship_System.program.dto.ProgramCreateRequest;
import com.example.Internship_System.program.dto.ProgramUpdateRequest;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.program.service.ProgramService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    // ============= SEARCH ==================
    @GetMapping("/search")
    public List<Program> searchProgram(@RequestParam String name) {
        return programService.searchByName(name);
    }

    // FILTER BY DEPARTMENT
    @GetMapping("/filter/department")
    public List<Program> filterByDepartment(@RequestParam String department) {
        return programService.filterByDepartment(department);
    }

    // FILTER BY MENTOR
    @GetMapping("/filter/mentor")
    public List<MentorProgramDTO> filterByMentor(@RequestParam Integer mentorId) {
        return programService.filterByMentor(mentorId);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPrograms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy, // optional sorting field
            @RequestParam(defaultValue = "asc") String sortDir  // "asc" or "desc"
    ) {
        int pageIndex = page - 1; // Spring pages are 0-based
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageIndex, size, sort);

        Page<Program> programPage = programService.getAllPrograms(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", programPage.getContent());
        response.put("currentPage", programPage.getNumber() + 1);
        response.put("totalItems", programPage.getTotalElements());
        response.put("totalPages", programPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // Delete program by ID
    @DeleteMapping("/{programId}")
    public ResponseEntity<?> deleteProgram(@PathVariable Integer programId) {
        programService.deleteProgram(programId);
        return ResponseEntity.ok("Program deleted successfully");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProgram(@RequestBody ProgramCreateRequest request) {
        try {
            Program program = programService.createProgram(request);
            return ResponseEntity.ok(program);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProgram(
            @PathVariable Integer id,
            @RequestBody ProgramUpdateRequest request
    ) {
        try {
            Program updated = programService.updateProgram(id, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{programId}/clone-template")
    public ProgramCloneDTO getCloneTemplate(@PathVariable Integer programId) {
        return programService.getCloneTemplate(programId);
    }

    @PostMapping("/clone")
    public Program cloneProgram(@RequestBody ProgramCreateRequest request) {
        return programService.cloneProgram(request);
    }

}
