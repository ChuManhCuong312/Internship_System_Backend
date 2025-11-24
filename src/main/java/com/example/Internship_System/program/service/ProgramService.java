package com.example.Internship_System.program.service;

import com.example.Internship_System.program.dto.MentorProgramDTO;
import com.example.Internship_System.program.dto.ProgramCreateRequest;
import com.example.Internship_System.program.dto.ProgramUpdateRequest;
import com.example.Internship_System.program.entity.MentorProgram;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.program.entity.ProgramStatus;
import com.example.Internship_System.repository.MentorProgramRepository;
import com.example.Internship_System.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final MentorProgramRepository mentorProgramRepository;

    public ProgramService(ProgramRepository programRepository, MentorProgramRepository mentorProgramRepository) {
        this.programRepository = programRepository;
        this.mentorProgramRepository = mentorProgramRepository;
    }

    // Search program by name
    public List<Program> searchByName(String name) {
        return programRepository.searchByName(name);
    }

    // Filter by department
    public List<Program> filterByDepartment(String department) {
        return programRepository.findByDepartmentIgnoreCase(department);
    }

    // Filter by mentor
    public List<MentorProgramDTO> filterByMentor(Integer mentorId) {
        List<MentorProgram> list = mentorProgramRepository.findByMentor_MentorId(mentorId);

        return list.stream()
                .map(mp -> new MentorProgramDTO(
                        mp.getProgram().getProgramId(),
                        mp.getProgram().getName(),
                        mp.getMentor().getUser().getFullName()
                ))
                .collect(Collectors.toList());
    }

    // Get all programs
    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    // Delete program by ID
    public void deleteProgram(Integer id) {
        programRepository.deleteById(id);
    }

    // Create new program
    public Program createProgram(ProgramCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        // RULE 1: startDate must be at least 2 weeks from now
        if (request.getStartDate().isBefore(now.plusWeeks(2))) {
            throw new IllegalArgumentException("Start date must be at least 2 weeks from today.");
        }

        // RULE 2: endDate must be at least 1 month after startDate
        if (request.getEndDate().isBefore(request.getStartDate().plusMonths(1))) {
            throw new IllegalArgumentException("End date must be at least 1 month after the start date.");
        }

        // Create program
        Program program = new Program(
                request.getName(),
                request.getDepartment(),
                request.getStartDate(),
                request.getEndDate(),
                request.getDetail(),
                request.getMaxInterns()
        );

        // Always UPCOMING
        program.setProgramStatus(ProgramStatus.UPCOMING);

        return programRepository.save(program);
    }

    public Program updateProgram(Integer id, ProgramUpdateRequest request) {

        Program program = programRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Program not found"));

        // ❌ Block editing if status is ON_GOING or FINISHED
        if (program.getProgramStatus() == ProgramStatus.ON_GOING ||
                program.getProgramStatus() == ProgramStatus.FINISHED) {
            throw new RuntimeException("Cannot edit this program because it is ON_GOING or FINISHED.");
        }

        // RULE 1: New start date must be at least 1 day after old start date
        if (request.getStartDate().isBefore(program.getStartDate().plusDays(1))) {
            throw new IllegalArgumentException("New start date must be at least 1 day after the previous start date.");
        }

        // RULE 2: End date must be at least 1 month after new start date
        if (request.getEndDate().isBefore(request.getStartDate().plusMonths(1))) {
            throw new IllegalArgumentException("End date must be at least one month after the new start date.");
        }

        // Apply updates
        program.setName(request.getName());
        program.setStartDate(request.getStartDate());
        program.setEndDate(request.getEndDate());
        program.setDetail(request.getDetail());
        program.setMaxInterns(request.getMaxInterns());

        return programRepository.save(program);
    }
}
