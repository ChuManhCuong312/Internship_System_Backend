package com.example.Internship_System.team.controller;

import com.example.Internship_System.team.dto.AutoTeamResultDTO;
import com.example.Internship_System.team.dto.CreateAutoTeamsRequest;
import com.example.Internship_System.team.dto.InternAutoDTO;
import com.example.Internship_System.team.service.TeamAutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs/{programId}/auto-teams")
public class TeamAutoController {

    private final TeamAutoService teamAutoService;

    public TeamAutoController(TeamAutoService teamAutoService) {
        this.teamAutoService = teamAutoService;
    }

    // GET /api/programs/{programId}/auto-teams/interns?name=abc
    @GetMapping("/interns/auto")
    public List<InternAutoDTO> getAvailableInterns(@PathVariable Integer programId) {
        return teamAutoService.getAvailableInterns();
    }

    // GET /api/programs/{programId}/auto-teams/interns/filter?major=CS
    @GetMapping("/interns/auto/filter")
    public List<InternAutoDTO> filterAvailableInternsByMajor(
            @PathVariable Integer programId,
            @RequestParam String major) {

        return teamAutoService.getAvailableInternsByMajor(major);
    }

    // POST /api/programs/{programId}/auto-teams/create
    // Body: CreateAutoTeamsRequest (programId can be included or inferred from path)
    @PostMapping("/interns/auto/create")
    public List<AutoTeamResultDTO> createAutoTeams(
            @PathVariable Integer programId,
            @RequestBody CreateAutoTeamsRequest req) {

        // ensure path programId and request programId align (optional)
        if (req.getProgramId() == null) req.setProgramId(programId);
        else if (!req.getProgramId().equals(programId)) {
            throw new RuntimeException("programId mismatch");
        }

        return teamAutoService.createAutoTeams(req);
    }
}
