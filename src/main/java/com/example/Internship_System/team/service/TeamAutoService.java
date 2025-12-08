package com.example.Internship_System.team.service;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.intern.entity.InternProfile;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TeamAutoService {

    private final InternRepository internRepository;
    private final TeamInternRepository teamInternRepository;
    private final TeamRepository teamRepository;
    private final ProgramRepository programRepository;
    private final UserRepository userRepository;

    public TeamAutoService(InternRepository internRepository,
                           TeamInternRepository teamInternRepository,
                           TeamRepository teamRepository,
                           ProgramRepository programRepository,
                           UserRepository userRepository) {
        this.internRepository = internRepository;
        this.teamInternRepository = teamInternRepository;
        this.teamRepository = teamRepository;
        this.programRepository = programRepository;
        this.userRepository = userRepository;
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
                .orElseThrow(() -> new RuntimeException("Program not found"));

        // Program must be UPCOMING
        if (program.getProgramStatus() != com.example.Internship_System.program.entity.ProgramStatus.UPCOMING) {
            throw new RuntimeException("Cannot auto-create teams: program is not UPCOMING");
        }

        int nInterns = chosenInternIds.size();
        if (nInterns == 0) {
            throw new RuntimeException("No interns selected");
        }
        if (numTeams <= 0) {
            throw new RuntimeException("Number of teams must be >= 1");
        }
        if (numTeams > nInterns) {
            throw new RuntimeException("Number of teams cannot exceed number of selected interns");
        }

        int programMaxInterns = program.getMaxInterns();
        int alreadyAssignedInterns = teamInternRepository.countInternsByProgram(programId);

        // Validate total after auto team creation
        if (nInterns + alreadyAssignedInterns > programMaxInterns) {
            throw new RuntimeException(
                    "Cannot create teams: selected interns exceed remaining available slots in program. " +
                            "Remaining quota = " + (programMaxInterns - alreadyAssignedInterns)
            );
        }

        // Ensure all interns are still available (not assigned to any team)
        for (Integer internId : chosenInternIds) {
            if (teamInternRepository.existsByIntern_InternId(internId)) {
                throw new RuntimeException("Intern " + internId + " is already assigned to a team");
            }
        }

        // Shuffle interns for randomness
        List<Integer> shuffled = new ArrayList<>(chosenInternIds);
        Collections.shuffle(shuffled, new Random());

        int base = nInterns / numTeams;
        int remainder = nInterns % numTeams;

        List<AutoTeamResultDTO> results = new ArrayList<>();
        int idx = 0;

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
                        .orElseThrow(() -> new RuntimeException("Intern not found: " + internId));
                TeamIntern ti = new TeamIntern(savedTeam, intern, LocalDateTime.now());
                teamInternRepository.save(ti);
            }

            results.add(new AutoTeamResultDTO(savedTeam.getTeamId(), assigned));
        }

        return results;
    }
}
