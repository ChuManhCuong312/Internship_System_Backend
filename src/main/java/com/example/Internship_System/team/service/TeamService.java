package com.example.Internship_System.team.service;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.team.dto.CreateTeamDTO;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.program.entity.ProgramStatus;
import com.example.Internship_System.repository.*;
import com.example.Internship_System.team.dto.TeamInfoDTO;
import com.example.Internship_System.team.dto.TeamInternInfoDTO;
import com.example.Internship_System.team.entity.Team;
import com.example.Internship_System.team.entity.TeamIntern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamInternRepository teamInternRepository;

    @Autowired
    private InternRepository internRepository; // To get email, phone from User

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private MentorRepository mentorRepository;

    public List<TeamInfoDTO> getTeamsInProgram(Integer programId) {
        List<Team> teams = teamRepository.findByProgramProgramId(programId);

        return teams.stream().map(team -> {
            String mentorName = team.getMentor().getUser().getFullName();

            List<TeamIntern> teamInterns = teamInternRepository.findByTeamTeamId(team.getTeamId());

            List<TeamInternInfoDTO> internDTOs = teamInterns.stream()
                    .map(ti -> {
                        InternProfile intern = ti.getIntern();
                        User user = userRepository.findById(intern.getUserId()).orElse(null);

                        return new TeamInternInfoDTO(
                                user.getFullName(),
                                user.getEmail(),
                                user.getPhone()
                        );
                    })
                    .toList();

            return new TeamInfoDTO(team.getTeamId(), mentorName, internDTOs);

        }).toList();
    }

    public Team assignMentorToProgram(Integer programId, Integer mentorId) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));
        if (program.getProgramStatus() == ProgramStatus.ON_GOING ||
                program.getProgramStatus() == ProgramStatus.FINISHED) {
            throw new RuntimeException("Cannot assign mentor to  this program because it is FINISHED.");
        }

        MentorUser mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        // Prevent duplicate assignment
        boolean exists = teamRepository.existsByProgramProgramIdAndMentorMentorId(programId, mentorId);
        if (exists) {
            throw new RuntimeException("This mentor is already assigned to this program");
        }

        Team team = new Team(program, mentor, LocalDateTime.now());
        return teamRepository.save(team);
    }

    public Team createTeam(CreateTeamDTO dto) {

        Program program = programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new RuntimeException("Program not found"));
        if (program.getProgramStatus() == ProgramStatus.ON_GOING ||
                program.getProgramStatus() == ProgramStatus.FINISHED) {
            throw new RuntimeException("Cannot create team for  this program because it is FINISHED.");
        }

        // Validate max interns
        int currentInterns = teamInternRepository.countInterns(dto.getProgramId());
        int incoming = dto.getInternIds().size();

        if (currentInterns + incoming > program.getMaxInterns()) {
            throw new RuntimeException("Too many interns! Exceeds program max interns.");
        }

        MentorUser mentor = mentorRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        Team team = new Team(program, mentor, LocalDateTime.now());
        Team savedTeam = teamRepository.save(team);

        for (Integer internId : dto.getInternIds()) {
            InternProfile intern = internRepository.findById(internId)
                    .orElseThrow(() -> new RuntimeException("Intern not found"));

            TeamIntern ti = new TeamIntern(savedTeam, intern, LocalDateTime.now());
            teamInternRepository.save(ti);
        }

        return savedTeam;
    }
}
