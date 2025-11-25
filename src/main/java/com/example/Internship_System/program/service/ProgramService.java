package com.example.Internship_System.program.service;

import com.example.Internship_System.program.dto.MentorProgramDTO;
import com.example.Internship_System.program.dto.ProgramCloneDTO;
import com.example.Internship_System.program.dto.ProgramCreateRequest;
import com.example.Internship_System.program.dto.ProgramUpdateRequest;
import com.example.Internship_System.program.entity.MentorProgram;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.program.entity.ProgramStatus;
import com.example.Internship_System.repository.MentorProgramRepository;
import com.example.Internship_System.repository.ProgramRepository;
import com.example.Internship_System.repository.TeamInternRepository;
import com.example.Internship_System.repository.TeamRepository;
import com.example.Internship_System.team.entity.Team;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final MentorProgramRepository mentorProgramRepository;
    private final TeamRepository teamRepository;
    private final TeamInternRepository teamInternRepository;

    public ProgramService(ProgramRepository programRepository,
                          MentorProgramRepository mentorProgramRepository,
                          TeamRepository teamRepository,
                          TeamInternRepository teamInternRepository) {
        this.programRepository = programRepository;
        this.mentorProgramRepository = mentorProgramRepository;
        this.teamRepository = teamRepository;
        this.teamInternRepository = teamInternRepository;
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
    @Transactional
    public void deleteProgram(Integer programId) {

        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        // ❌ Cannot delete ongoing or finished program
        if (!program.getProgramStatus().equals(ProgramStatus.UPCOMING)) {
            throw new RuntimeException("Cannot delete program because it is ON_GOING or FINISHED");
        }

        // 1️⃣ Delete team_intern mappings for all teams
        List<Team> teams = teamRepository.findByProgramProgramId(programId);

        for (Team team : teams) {
            teamInternRepository.deleteAllByTeam_TeamId(team.getTeamId());
        }

        // 2️⃣ Delete all teams
        teamRepository.deleteAll(teams);

        // 3️⃣ Delete mentor-program associations
        mentorProgramRepository.deleteByProgram_ProgramId(programId);

        // 4️⃣ Delete program itself
        programRepository.delete(program);
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

    public ProgramCloneDTO getCloneTemplate(Integer programId) {
        Program p = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        ProgramCloneDTO dto = new ProgramCloneDTO();
        dto.setName(p.getName() + " (copy)");
        dto.setDepartment(p.getDepartment());
        dto.setDetails(p.getDetail());
        dto.setMaxInterns(p.getMaxInterns());

        return dto;
    }

    @Transactional
    public Program cloneProgram(ProgramCreateRequest request) {

        LocalDateTime start = request.getStartDate();
        LocalDateTime end = request.getEndDate();

        if (start.isBefore(LocalDateTime.now().plusWeeks(2))) {
            throw new RuntimeException("Start date must be at least 2 weeks from now");
        }

        if (end.isBefore(start.plusMonths(1))) {
            throw new RuntimeException("End date must be at least 1 month after start date");
        }

        Program newProgram = new Program();
        newProgram.setName(request.getName());
        newProgram.setDepartment(request.getDepartment());
        newProgram.setDetail(request.getDetail());
        newProgram.setMaxInterns(request.getMaxInterns());
        newProgram.setStartDate(start);
        newProgram.setEndDate(end);
        newProgram.setProgramStatus(ProgramStatus.UPCOMING);

        return programRepository.save(newProgram);
    }

}
