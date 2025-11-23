package com.example.Internship_System.program.service;

import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    public Program getProgramById(Integer id) {
        Optional<Program> program = programRepository.findById(id);
        return program.orElse(null);
    }
}
