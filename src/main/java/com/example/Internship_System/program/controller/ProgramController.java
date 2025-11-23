package com.example.Internship_System.program.controller;

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

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Program>> getAllPrograms() {
        List<Program> programs = programService.getAllPrograms();
        return ResponseEntity.ok(programs);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Program> getProgramById(@PathVariable Integer id) {
        Program program = programService.getProgramById(id);

        if (program == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(program);
    }
}

