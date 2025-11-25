package com.example.Internship_System.program.controller;

import com.example.Internship_System.program.dto.MentorProgramDTO;
import com.example.Internship_System.program.dto.ProgramCloneDTO;
import com.example.Internship_System.program.dto.ProgramCreateRequest;
import com.example.Internship_System.program.dto.ProgramUpdateRequest;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.program.service.ProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Get all programs
    @GetMapping
    public List<Program> getAllPrograms() {
        return programService.getAllPrograms();
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

    @PutMapping("/update/{id}")
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
