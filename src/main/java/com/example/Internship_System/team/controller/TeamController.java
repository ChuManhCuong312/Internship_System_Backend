package com.example.Internship_System.team.controller;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.team.dto.CreateTeamDTO;
import com.example.Internship_System.repository.InternRepository;
import com.example.Internship_System.repository.MentorRepository;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.team.dto.*;
import com.example.Internship_System.team.entity.Team;
import com.example.Internship_System.team.service.MentorProgramService;
import com.example.Internship_System.team.service.ProgramStatisticsService;
import com.example.Internship_System.team.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final ProgramStatisticsService programStatisticsService;
    private final TeamService teamService;
    private final MentorProgramService mentorProgramService;
    private final MentorRepository mentorRepository;
    private final InternRepository internRepository;
    private final UserRepository userRepository;

    public TeamController(ProgramStatisticsService programStatisticsService,
                          TeamService teamService,
                          MentorProgramService mentorProgramService,
                          MentorRepository mentorRepository,
                          InternRepository internRepository,
                          UserRepository userRepository){
        this.programStatisticsService = programStatisticsService;
        this.teamService = teamService;
        this.mentorProgramService = mentorProgramService;
        this.mentorRepository = mentorRepository;
        this.internRepository = internRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{programId}/overview")
    public ProgramOverviewDTO getProgramStats(@PathVariable Integer programId) {
        return programStatisticsService.getProgramOverview(programId);
    }

    @GetMapping("/{programId}/teams")
    public List<TeamInfoDTO> getTeams(@PathVariable Integer programId) {
        return teamService.getTeamsInProgram(programId);
    }

    @GetMapping("/{programId}/mentors")
    public List<MentorInfoDTO> getMentorsForProgram(@PathVariable Integer programId) {
        return mentorProgramService.getProgramMentorInfo(programId);
    }

    @GetMapping("/mentors/search")
    public List<MentorInfoDTO> searchMentor(@RequestParam String name) {
        return mentorRepository.searchMentorByName(name)
                .stream()
                .map(m -> new MentorInfoDTO(
                        m.getMentorId(),                      // include ID
                        m.getUser().getFullName(),            // full name
                        m.getUser().getEmail(),
                        m.getUser().getPhone(),
                        m.getDepartment(),
                        m.getExpertise()
                ))
                .toList();
    }

    @PostMapping("/assign-mentor")
    public ResponseEntity<?> assignMentorToProgram(@RequestBody AssignMentorDTO dto) {
        Team team = teamService.assignMentorToProgram(dto.getProgramId(), dto.getMentorId());
        return ResponseEntity.ok(team);
    }

    @GetMapping("/interns/search")
    public List<TeamInternInfoDTO> searchIntern(@RequestParam String name) {
        return internRepository.searchInternsByName(name)
                .stream()
                .map(i -> {
                    User u = userRepository.findById(i.getUserId()).orElseThrow();
                    return new TeamInternInfoDTO(u.getFullName(), u.getEmail(), u.getPhone());
                })
                .toList();
    }

    @PostMapping("/teams/create")
    public ResponseEntity<?> createTeam(@RequestBody CreateTeamDTO dto) {
        return ResponseEntity.ok(teamService.createTeam(dto));
    }

    @DeleteMapping("/{programId}/mentors/{mentorId}")
    public ResponseEntity<?> removeMentorFromProgram(
            @PathVariable Integer programId,
            @PathVariable Integer mentorId) {

        teamService.removeMentorFromProgram(programId, mentorId);
        return ResponseEntity.ok("Mentor removed from program successfully");
    }

    @DeleteMapping("/teams/{teamId}/interns/{internId}")
    public ResponseEntity<?> removeInternFromTeam(
            @PathVariable Integer teamId,
            @PathVariable Integer internId) {

        teamService.removeInternFromTeam(teamId, internId);
        return ResponseEntity.ok("Intern removed from team");
    }

    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Integer teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok("Team deleted successfully");
    }

    @PutMapping("/teams/{teamId}")
    public ResponseEntity<?> updateTeam(
            @PathVariable Integer teamId,
            @RequestBody UpdateTeamRequestDTO request) {

        teamService.updateTeam(teamId, request);
        return ResponseEntity.ok("Team updated successfully");
    }
}
