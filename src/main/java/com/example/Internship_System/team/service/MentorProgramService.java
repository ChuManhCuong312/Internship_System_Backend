package com.example.Internship_System.team.service;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.repository.MentorProgramRepository;
import com.example.Internship_System.repository.TeamRepository;
import com.example.Internship_System.team.dto.MentorInfoDTO;
import com.example.Internship_System.team.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MentorProgramService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MentorProgramRepository mentorProgramRepository;

    public List<MentorInfoDTO> getProgramMentorInfo(Integer programId) {
        List<Team> teams = teamRepository.findByProgramProgramId(programId);

        return teams.stream()
                .map(Team::getMentor)
                .filter(Objects::nonNull)
                .distinct() // ensures each mentor appears only once
                .map(mentor -> {
                    User user = mentor.getUser();
                    return new MentorInfoDTO(
                            mentor.getMentorId(),
                            user.getFullName(),
                            user.getEmail(),
                            user.getPhone(),
                            mentor.getDepartment(),
                            mentor.getExpertise()
                    );
                })
                .toList();
    }

    public List<MentorInfoDTO> getAllMentorsAssignedToProgram(Integer programId) {

        return mentorProgramRepository.findByProgram_ProgramId(programId).stream()
                .map(mp -> {
                    MentorUser mentor = mp.getMentor();
                    User user = mentor.getUser();

                    return new MentorInfoDTO(
                            mentor.getMentorId(),
                            user.getFullName(),
                            user.getEmail(),
                            user.getPhone(),
                            mentor.getDepartment(),
                            mentor.getExpertise()
                    );
                })
                .toList();
    }

    public MentorInfoDTO getMentorByTeam(Integer teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        MentorUser mentor = team.getMentor();
        var user = mentor.getUser();

        return new MentorInfoDTO(
                mentor.getMentorId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                mentor.getDepartment(),
                mentor.getExpertise()
        );
    }
}
