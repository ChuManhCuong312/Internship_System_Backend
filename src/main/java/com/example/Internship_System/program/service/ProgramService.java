package com.example.Internship_System.program.service;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.auth.entity.UserStatus;
import com.example.Internship_System.event.entity.ProgramEvent;
import com.example.Internship_System.event.repository.ProgramEventRepository;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.notification.service.NotificationService;
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
import org.apache.xmlbeans.impl.values.XmlIntegerRestriction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Internship_System.team.entity.TeamIntern;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
    private final InternRepository internRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ProgramEventRepository programEventRepository;


    public ProgramService(ProgramRepository programRepository,
                          MentorProgramRepository mentorProgramRepository,
                          TeamRepository teamRepository,
                          TeamInternRepository teamInternRepository,
                          TaskTeamAssignmentRepository taskTeamAssignmentRepository,
                          TaskRepository taskRepository,
                          MentorRepository mentorRepository,
                          InternRepository internRepository,
                          UserRepository userRepository,
                          NotificationService notificationService,
                          ProgramEventRepository programEventRepository ) {
        this.programRepository = programRepository;
        this.mentorProgramRepository = mentorProgramRepository;
        this.teamRepository = teamRepository;
        this.teamInternRepository = teamInternRepository;
        this.taskTeamAssignmentRepository = taskTeamAssignmentRepository;
        this.taskRepository = taskRepository;
        this.mentorRepository = mentorRepository;
        this.internRepository = internRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.programEventRepository = programEventRepository;
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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình"));

        // ❌ Cannot delete ongoing program
        if (program.getProgramStatus().equals(ProgramStatus.ON_GOING)) {
            throw new RuntimeException("Không thể xóa chương trình khi đang diễn ra");
        }

        String programName = program.getName();

        List<Team> teams = teamRepository.findByProgramProgramId(programId);

        // Notify all interns in all teams
        for (Team team : teams) {
            List<TeamIntern> teamInterns =
                    teamInternRepository.findAllByTeam_TeamId(team.getTeamId());

            for (TeamIntern ti : teamInterns) {
                notificationService.createProgramDeletedNotification(
                        ti.getIntern().getInternId(),
                        programName
                );
            }
        }

        // 1️⃣ Delete team_intern mappings for all teams
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

        if(request.getStartDate() == null){
            throw new IllegalArgumentException("Ngày bắt đầu không được để trống.");
        }

        if(request.getEndDate() == null){
            throw new IllegalArgumentException("Ngày kết thúc không được để trống.");
        }

        // RULE 1: startDate must be at least 2 weeks from now
        if (request.getStartDate().isBefore(now.plusWeeks(2))) {
            throw new IllegalArgumentException("Ngày bắt đầu phải sau ngày hôm nay ít nhất 2 tuần.");
        }

        // RULE 2: endDate must be at least 1 month after startDate
        if (request.getEndDate().isBefore(request.getStartDate().plusMonths(1))) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu 1 tháng.");
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
            throw new RuntimeException("Không thể cập chương khi đang diễn ra hoặc đã kết thúc.");
        }

        if(request.getStartDate() == null){
            throw new IllegalArgumentException("Ngày bắt đầu không được để trống.");
        }

        if(request.getEndDate() == null){
            throw new IllegalArgumentException("Ngày kết thúc không được để trống.");
        }

        // RULE 1: New start date CANNOT be BEFORE old start date
        if (request.getStartDate().isBefore(program.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc mới không thể trước ngày kết thúc cũ.");
        }

        // RULE 2: End date must be at least 1 month after new start date
        if (request.getEndDate().isBefore(request.getStartDate().plusMonths(1))) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu mới 1 tháng.");
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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình"));

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

        if(start == null){
            throw new IllegalArgumentException("Ngày bắt đầu không được để trống.");
        }

        if(end == null){
            throw new IllegalArgumentException("Ngày kết thúc không được để trống.");
        }

        if (start.isBefore(LocalDateTime.now().plusWeeks(2))) {
            throw new RuntimeException("Ngày bắt đầu phải sau ngày hôm nay ít nhất 2 tuần.");
        }

        if (end.isBefore(start.plusMonths(1))) {
            throw new RuntimeException("Ngày kết thúc phải sau ngày bắt đầu 1 tháng.");
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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình"));

        // ❌ Block assignment if program is ongoing or finished
        if (program.getProgramStatus() == ProgramStatus.FINISHED) {
            throw new RuntimeException("Không thể phân công mentor khi chương trình đã kết thúc.");
        }

        MentorUser mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mentor"));

        // ❌ Prevent duplicate assignment
        boolean exists = mentorProgramRepository
                .existsByProgram_ProgramIdAndMentor_MentorId(programId, mentorId);

        if (exists) {
            throw new RuntimeException("Mentor đã được phân công cho chương trình này.");
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
        // 1.5 PROGRAM EVENTS (NEW) ✅
        // =============================
        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("HH:mm");

        List<ProgramEvent> programEvents =
                programEventRepository.findByProgramId(program.getProgramId());

        for (ProgramEvent e : programEvents) {

            LocalDateTime startDateTime =
                    LocalDateTime.of(e.getEventDate(), e.getStartTime());

            String timeRange = e.getStartTime().format(timeFormatter)
                    + " - "
                    + e.getEndTime().format(timeFormatter);

            String fullDescription =
                    "⏰ " + timeRange +
                            (e.getDescription() != null && !e.getDescription().isBlank()
                                    ? "\n" + e.getDescription()
                                    : "");

            events.add(new ScheduleEventDTO(
                    "event-" + e.getEventId(),
                    "Sự kiện: "+e.getTitle(),
                    "Địa điểm: "+e.getLocation(),     // subtitle
                    "task",
                    startDateTime,
                    fullDescription
            ));
        }

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


        // =============================
        // 3. SẮP XẾP THEO THỜI GIAN (SỚM → MUỘN)
        // =============================
        // Giả định ScheduleEventDTO có getter: getDateTime() (hoặc tên trường bạn đang dùng).
        // Nếu tên khác (ví dụ getDate()), sửa lại cho khớp.
        Comparator<ScheduleEventDTO> byTimeAsc = Comparator
                .comparing(
                        ScheduleEventDTO::getDate,               // <-- thay bằng getter đúng
                        Comparator.nullsLast(Comparator.naturalOrder())
                )
                // (tuỳ chọn) nếu cùng thời điểm, ưu tiên "program" trước "task"/"deadline"
                .thenComparing(
                        ScheduleEventDTO::getType,
                        Comparator.nullsLast(Comparator.naturalOrder())
                );

        Collections.sort(events, byTimeAsc);

        return events;

    }

    private void updateStatuses() {
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
     * Calculates status based on timeline
     */
    private ProgramStatus calculateStatus(Program p) {
        LocalDateTime today = LocalDateTime.now();

        // HR must manually change to FINISHED
        if (p.getProgramStatus() == ProgramStatus.FINISHED) {
            return ProgramStatus.FINISHED;
        }

        if (today.isBefore(p.getStartDate())) {
            return ProgramStatus.UPCOMING;
        } else {
            return ProgramStatus.ON_GOING;
        }
    }


    /**
     * Runs ONCE when backend starts
     */
    @PostConstruct
    @Transactional
    public void updateStatusesOnStartup() {
        System.out.println("Running startup program status update...");
        updateStatuses();
    }

    /**
     * Runs EVERY DAY at midnight
     * Format = second minute hour day month dayOfWeek
     * 0 0 0 → 00:00:00 daily
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateStatusesDaily() {
        System.out.println("Running scheduled program status update...");
        updateStatuses();
    }



    public List<Program> getOngoingProgramsByUser(Integer userId) {
        return programRepository.findOngoingProgramsByMentorUserId(userId);
    }
    public List<ProgramSimpleDTO> getSimpleProgramListByUser(Integer userId) {
        List<Program> programs = getOngoingProgramsByUser(userId);

        return programs.stream().map(p -> {
            ProgramSimpleDTO dto = new ProgramSimpleDTO();
            dto.setProgram_id(p.getProgramId());
            dto.setProgram_name(p.getName());
            dto.setDescription(p.getDetail());
            dto.setStart_date(p.getStartDate().toString());
            dto.setEnd_date(p.getEndDate().toString());
            return dto;
        }).toList();
    }

    @Transactional
    public int finishProgram(Integer programId) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình"));

        if (program.getProgramStatus() == ProgramStatus.FINISHED) {
            throw new RuntimeException("Chương trình đã kết thúc");
        }

        // Set program to FINISHED
        program.setProgramStatus(ProgramStatus.FINISHED);
        programRepository.save(program);

        // Get all interns assigned to teams in this program
        List<InternProfile> internProfiles =
                internRepository.findInternsByProgramId(programId);

        int count = 0;
        for (InternProfile intern : internProfiles) {
            User user = userRepository.findById(intern.getUserId()).orElse(null);
            if (user != null) {
                user.setStatus(UserStatus.REJECTED); // update
                userRepository.save(user);
                count++;
            }
        }

        return count;
    }

}
