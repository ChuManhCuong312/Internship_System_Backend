package com.example.Internship_System.program.service;

import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.program.dto.*;
import com.example.Internship_System.program.entity.MentorProgram;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.program.entity.ProgramStatus;
import com.example.Internship_System.repository.*;
import com.example.Internship_System.task.entity.Task;
import com.example.Internship_System.repository.TaskRepository;
import com.example.Internship_System.repository.TaskTeamAssignmentRepository;
import com.example.Internship_System.team.entity.Team;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Internship_System.team.entity.TeamIntern;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final MentorProgramRepository mentorProgramRepository;
    private final TeamRepository teamRepository;
    private final TeamInternRepository teamInternRepository;
    private final TaskTeamAssignmentRepository taskTeamAssignmentRepository;
    private final TaskRepository taskRepository;
    private final MentorRepository mentorRepository;

    public ProgramService(ProgramRepository programRepository,
                          MentorProgramRepository mentorProgramRepository,
                          TeamRepository teamRepository,
                          TeamInternRepository teamInternRepository,
                          TaskTeamAssignmentRepository taskTeamAssignmentRepository,
                          TaskRepository taskRepository,
                          MentorRepository mentorRepository) {
        this.programRepository = programRepository;
        this.mentorProgramRepository = mentorProgramRepository;
        this.teamRepository = teamRepository;
        this.teamInternRepository = teamInternRepository;
        this.taskTeamAssignmentRepository = taskTeamAssignmentRepository;
        this.taskRepository = taskRepository;
        this.mentorRepository = mentorRepository;
    }

    // Search program by name
    public List<Program> searchByName(String name) {
        return programRepository.searchByName(name);
    }

    // Filter by department
    public List<Program> filterByDepartment(String department) {
        return programRepository.findByDepartmentIgnoreCase(department);
    }

    public List<String> getAllDepartments() {
        List<String> departments = programRepository.findDistinctDepartments();

        return departments.stream()
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
    }

    // Filter by mentor
    // Filter programs by mentor (returns full Program objects)
    public List<Program> filterProgramsByMentor(Integer mentorId) {
        List<MentorProgram> list = mentorProgramRepository.findByMentor_MentorId(mentorId);

        // Extract Programs and remove duplicates if the same mentor is assigned multiple times
        return list.stream()
                .map(MentorProgram::getProgram)
                .distinct()
                .toList();
    }

    public List<MentorDropdownDTO> getAssignedMentorsForDropdown() {
        List<MentorUser> mentors = mentorProgramRepository.findDistinctAssignedMentors();

        return mentors.stream()
                .map(m -> new MentorDropdownDTO(
                        m.getMentorId(),
                        m.getUser().getFullName()
                ))
                .sorted(Comparator.comparing(MentorDropdownDTO::getFullName))
                .collect(Collectors.toList());
    }
    // Get all programs
    public Page<Program> getAllPrograms(Pageable pageable) {
        return programRepository.findAll(pageable);
    }

    // Delete program by ID
    @Transactional
    public void deleteProgram(Integer programId) {

        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        // ❌ Cannot delete ongoing program
        if (program.getProgramStatus().equals(ProgramStatus.ON_GOING)) {
            throw new RuntimeException("Cannot delete program because it is ON_GOING");
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

        // RULE 1: New start date CANNOT be BEFORE old start date
        if (request.getStartDate().isBefore(program.getStartDate())) {
            throw new IllegalArgumentException("New start date cannot be earlier than the previous start date.");
        }

        // RULE 2: End date must be at least 1 month after new start date
        if (request.getEndDate().isBefore(request.getStartDate().plusMonths(1))) {
            throw new IllegalArgumentException("End date must be at least one month after the new start date.");
        }

        // Apply updates
        program.setName(request.getName());
        program.setDepartment(request.getDepartment());
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

    @Transactional
    public MentorProgram assignMentorToProgram(Integer programId, Integer mentorId) {

        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        // ❌ Block assignment if program is ongoing or finished
        if (program.getProgramStatus() == ProgramStatus.FINISHED) {
            throw new RuntimeException("Cannot assign mentor because the program is FINISHED.");
        }

        MentorUser mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        // ❌ Prevent duplicate assignment
        boolean exists = mentorProgramRepository
                .existsByProgram_ProgramIdAndMentor_MentorId(programId, mentorId);

        if (exists) {
            throw new RuntimeException("This mentor is already assigned to this program.");
        }

        // Create new mentor-program link
        MentorProgram mp = new MentorProgram();
        mp.setProgram(program);
        mp.setMentor(mentor);
        mp.setAssignedDate(LocalDateTime.now());

        return mentorProgramRepository.save(mp);
    }


    public List<ScheduleEventDTO> getProgramByInternId(Integer internId) {

        List<ScheduleEventDTO> events = new ArrayList<>();

        // =============================
        // 1. PROGRAM START + END
        // =============================
        Program program = programRepository.findProgramByInternId(internId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        // START
        events.add(new ScheduleEventDTO(
                program.getProgramId() + "-start",
                "Bắt đầu chương trình: " +program.getName(),
                "Bắt đầu " + program.getName(),
                "program",
                program.getStartDate(),
                program.getDetail()
        ));

        // END
        events.add(new ScheduleEventDTO(
                program.getProgramId() + "-end",
                "Kết thúc chương trình: " +program.getName(),
                "Kết thúc " + program.getName(),
                "program",
                program.getEndDate(),
                program.getDetail()
        ));

        // =============================
// 2. TASK CỦA TEAM INTERN
// =============================

// (1) Lấy team intern đang thuộc về
        TeamIntern teamIntern = teamInternRepository.findByInternId(internId);
        Integer teamId = teamIntern.getTeam().getTeamId();


        if (teamId != null) {

            // (2) Lấy taskId thuộc team (dùng instance, không phải class)
            List<Integer> teamTaskIds = taskTeamAssignmentRepository.findTaskIdsByTeamId(teamId);

            if (!teamTaskIds.isEmpty()) {

                // (3) Lấy danh sách task (dùng instance)
                List<Task> teamTasks = taskRepository.findByTaskIdIn(teamTaskIds);

                // (4) Format thành event schedule
                for (Task t : teamTasks) {
                    events.add(new ScheduleEventDTO(
                            "task-" + t.getTaskId(),
                            "Deadline: "+t.getTitle(),
                            "Deadline cho task: "+t.getTitle(),
                            "deadline",              // type = deadline
                            t.getDeadline(),
                            t.getDescription()
                    ));
                }
            }
        }

        return events;
    }



    @PostConstruct
    @Transactional
    public void updateStatusesOnStartup() {
        List<Program> programs = programRepository.findAll();

        for (Program p : programs) {
            ProgramStatus newStatus = calculateStatus(p);
            if (p.getProgramStatus() != newStatus) {
                p.setProgramStatus(newStatus);
                programRepository.save(p);
            }
        }
    }

    /**
     * Calculates the status of a program based on the current date
     */
    private ProgramStatus calculateStatus(Program p) {
        LocalDateTime today = LocalDateTime.now();
        if (today.isBefore(p.getStartDate())) {
            return ProgramStatus.UPCOMING;
        } else if (!today.isAfter(p.getEndDate())) {
            return ProgramStatus.ON_GOING;
        } else {
            return ProgramStatus.FINISHED;
        }
    }
}
