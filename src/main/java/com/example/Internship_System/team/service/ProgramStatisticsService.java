package com.example.Internship_System.team.service;

import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.repository.TeamInternRepository;
import com.example.Internship_System.repository.TeamRepository;
import com.example.Internship_System.team.dto.ProgramOverviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramStatisticsService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamInternRepository teamInternRepository;

    public ProgramOverviewDTO getProgramOverview(Integer programId) {
        int totalTeams = teamRepository.countTeams(programId);
        int totalInterns = teamInternRepository.countInterns(programId);

        List<MentorUser> mentors = teamRepository.findMentorsByProgram(programId);
        int totalMentors = mentors.size();

        List<String> mentorNames = mentors.stream()
                .map(m -> m.getUser().getFullName())   // MentorUser → User → name
                .toList();

        return new ProgramOverviewDTO(totalTeams, totalInterns, totalMentors, mentorNames);
    }
}

