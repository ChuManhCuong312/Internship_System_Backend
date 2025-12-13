package com.example.Internship_System.team.service;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.notification.service.NotificationService;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.repository.*;
import com.example.Internship_System.team.dto.AutoTeamResultDTO;
import com.example.Internship_System.team.dto.CreateAutoTeamsRequest;
import com.example.Internship_System.team.dto.InternAutoDTO;
import com.example.Internship_System.team.entity.Team;
import com.example.Internship_System.team.entity.TeamIntern;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamAutoService {

    private final InternRepository internRepository;
    private final TeamInternRepository teamInternRepository;
    private final TeamRepository teamRepository;
    private final ProgramRepository programRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public TeamAutoService(InternRepository internRepository,
                           TeamInternRepository teamInternRepository,
                           TeamRepository teamRepository,
                           ProgramRepository programRepository,
                           UserRepository userRepository,
                           NotificationService notificationService) {
        this.internRepository = internRepository;
        this.teamInternRepository = teamInternRepository;
        this.teamRepository = teamRepository;
        this.programRepository = programRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // Get available interns for table (name search)
    public List<InternAutoDTO> getAvailableInterns() {
        // Passing empty name will return ALL available interns
        List<InternProfile> interns = internRepository.searchAvailableInterns("");

        return interns.stream().map(i -> {
            User u = userRepository.findById(i.getUserId()).orElse(null);
            return new InternAutoDTO(
                    i.getInternId(),
                    i.getUserId(),
                    u != null ? u.getFullName() : "",
                    u != null ? u.getEmail() : "",
                    u != null ? u.getPhone() : "",
                    i.getGpa(),
                    i.getMajor(),
                    i.getSchool()
            );
        }).collect(Collectors.toList());
    }


    // Filter by major
    public List<InternAutoDTO> getAvailableInternsByMajor(String major) {
        List<InternProfile> interns = internRepository.findAvailableInternsByMajor(major);
        return interns.stream().map(i -> {
            User u = userRepository.findById(i.getUserId()).orElse(null);
            return new InternAutoDTO(i.getInternId(), i.getUserId(), u != null ? u.getFullName() : "",
                    u != null ? u.getEmail() : "", u != null ? u.getPhone() : "",
                    i.getGpa(), i.getMajor(), i.getSchool());
        }).collect(Collectors.toList());
    }


    @Transactional
    public List<AutoTeamResultDTO> createAutoTeams(CreateAutoTeamsRequest req) {
        Integer programId = req.getProgramId();
        List<Integer> chosenInternIds = req.getInternIds() == null ? List.of() : req.getInternIds();
        int numTeams = req.getNumberOfTeams() == null ? 0 : req.getNumberOfTeams();

        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình"));

        // Program must be UPCOMING
        if (program.getProgramStatus() != com.example.Internship_System.program.entity.ProgramStatus.UPCOMING) {
            throw new RuntimeException("Không thể tự động tạo teams: chương trình đã diễn ra hoặc đã kết thúc");
        }

        int nInterns = chosenInternIds.size();
        if (nInterns == 0) {
            throw new RuntimeException("Chưa có TTS được chọn");
        }
        if (numTeams <= 0) {
            throw new RuntimeException("Số lượng teams >= 1");
        }
        if (numTeams > nInterns) {
            throw new RuntimeException("Số lượng teams không thể vượt quá số TTS chọn");
        }

        int programMaxInterns = program.getMaxInterns();
        int alreadyAssignedInterns = teamInternRepository.countInternsByProgram(programId);

        // Validate total after auto team creation
        if (nInterns + alreadyAssignedInterns > programMaxInterns) {
            throw new RuntimeException(
                    "Không thể tạo teams: Số lượng TTS đã chọn vượt quá số lượng TTS còn trống của chương trình. " +
                            "Số lượng TTS còn trống = " + (programMaxInterns - alreadyAssignedInterns)
            );
        }

        // Ensure all interns are still available (not assigned to any team)
        for (Integer internId : chosenInternIds) {
            if (teamInternRepository.existsByIntern_InternId(internId)) {
                throw new RuntimeException("TTS " + internId + " đã được phân công đến 1 team");
            }
        }

        // Shuffle interns for randomness
        List<Integer> shuffled = new ArrayList<>(chosenInternIds);
        Collections.shuffle(shuffled, new Random());

        int base = nInterns / numTeams;
        int remainder = nInterns % numTeams;

        List<AutoTeamResultDTO> results = new ArrayList<>();
        int idx = 0;

        Map<Integer, List<Integer>> teamInternMap = new HashMap<>();

        for (int t = 0; t < numTeams; t++) {
            int size = base + (t < remainder ? 1 : 0);
            List<Integer> assigned = new ArrayList<>();

            // create team (mentor = null)
            Team team = new Team();
            team.setProgram(program);
            team.setMentor(null); // requires mentor_id nullable in DB
            team.setAssignedDate(LocalDateTime.now());
            Team savedTeam = teamRepository.save(team);

            for (int k = 0; k < size; k++) {
                Integer internId = shuffled.get(idx++);
                assigned.add(internId);
                // create team_intern row
                var intern = internRepository.findById(internId)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy TTS: " + internId));
                TeamIntern ti = new TeamIntern(savedTeam, intern, LocalDateTime.now());
                teamInternRepository.save(ti);
            }
            teamInternMap.put(savedTeam.getTeamId(), assigned);
            results.add(new AutoTeamResultDTO(savedTeam.getTeamId(), assigned));
        }

        for (var entry : teamInternMap.entrySet()) {
            Integer teamId = entry.getKey();
            for (Integer internId : entry.getValue()) {
                notificationService.createInternAddedToTeamNotification(
                        internId,
                        programId,
                        teamId
                );
            }
        }


        return results;
    }
}
