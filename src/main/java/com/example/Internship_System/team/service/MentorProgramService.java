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

@Service
public class MentorProgramService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MentorProgramRepository mentorProgramRepository;

    public List<MentorInfoDTO> getProgramMentorInfo(Integer programId) {

        List<Team> teams = teamRepository.findByProgramProgramId(programId);

        return teams.stream().map(team -> {
            MentorUser mentor = team.getMentor();
            User user = mentor.getUser();

            return new MentorInfoDTO(
                    mentor.getMentorId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone(),
                    mentor.getDepartment(),
                    mentor.getExpertise()
            );
        }).toList();
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
}
