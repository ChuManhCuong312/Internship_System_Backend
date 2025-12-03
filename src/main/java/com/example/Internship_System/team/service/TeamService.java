package com.example.Internship_System.team.service;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.team.dto.*;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.program.entity.ProgramStatus;
import com.example.Internship_System.repository.*;
import com.example.Internship_System.team.entity.Team;
import com.example.Internship_System.team.entity.TeamIntern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private MentorProgramRepository mentorProgramRepository;

    public List<TeamInfoDTO> getTeamsInProgram(Integer programId) {
        List<Team> teams = teamRepository.findByProgramProgramId(programId);

        return teams.stream().map(team -> {
            Integer mentorId = team.getMentor().getMentorId();
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

            return new TeamInfoDTO(team.getTeamId(), mentorId,mentorName, internDTOs);

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

    public TeamResponseDTO createTeam(CreateTeamDTO dto) {
        Program program = programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new RuntimeException("Program not found"));

        if (program.getProgramStatus() == ProgramStatus.ON_GOING ||
                program.getProgramStatus() == ProgramStatus.FINISHED) {
            throw new RuntimeException("Cannot create team for this program because it is FINISHED.");
        }

        MentorUser mentor = mentorRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        Team team = new Team(program, mentor, LocalDateTime.now());
        Team savedTeam = teamRepository.save(team);
        return new TeamResponseDTO(savedTeam);
    }


    @Transactional
    public void removeMentorFromProgram(Integer programId, Integer mentorId) {

        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        // ❌ Cannot modify if ongoing or finished
        if (!program.getProgramStatus().equals(ProgramStatus.UPCOMING)) {
            throw new RuntimeException("Cannot remove mentor because program is not UPCOMING");
        }

        // Check if mentor belongs to any team in this program
        boolean mentorHasTeam =
                teamRepository.existsByProgramProgramIdAndMentorMentorId(programId, mentorId);

        if (mentorHasTeam) {
            throw new RuntimeException("Cannot remove mentor. They are assigned to a team.");
        }

        // Remove from mentor_program table
        mentorProgramRepository.deleteByProgram_ProgramIdAndMentor_MentorId(programId, mentorId);
    }

    @Transactional
    public void removeInternFromTeam(Integer teamId, Integer internId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Remove mapping
        teamInternRepository.deleteByTeam_TeamIdAndIntern_InternId(teamId, internId);
    }

    @Transactional
    public void deleteTeam(Integer teamId) {

        if (!teamRepository.existsById(teamId)) {
            throw new RuntimeException("Team not found");
        }

        // Delete interns inside team
        teamInternRepository.deleteAllByTeam_TeamId(teamId);

        // Delete team
        teamRepository.deleteById(teamId);
    }

    @Transactional
    public void updateTeam(Integer teamId, UpdateTeamRequestDTO request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Program program = team.getProgram();

        boolean mentorInProgram = mentorProgramRepository
                .existsByProgram_ProgramIdAndMentor_MentorId(
                        program.getProgramId(),
                        request.getMentorId()
                );

        if (!mentorInProgram) {
            throw new RuntimeException("Mentor is not assigned to this program");
        }

        team.setMentor(mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found")));

        teamRepository.save(team);
    }


    public List<MentorInfoDTO> searchMentorsInProgram(Integer programId, String query) {
        return mentorProgramRepository.searchMentorsInProgram(programId, query)
                .stream()
                .map(mp -> {
                    var mentor = mp.getMentor();
                    var user = mentor.getUser();

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
    public List<TeamSimpleDTO> getSimpleTeamsByProgram(Integer programId) {
        List<Team> teams = teamRepository.findByProgramProgramId(programId);

        return teams.stream().map(team -> {
            TeamSimpleDTO dto = new TeamSimpleDTO();
            dto.setTeam_id(team.getTeamId());
            dto.setTeam_name("Nhóm " + team.getTeamId()); // Nếu muốn tên nhóm tùy biến
            int memberCount = teamInternRepository.countByTeamId(team.getTeamId());
            dto.setMember_count(memberCount);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<InternSearchDTO> searchAvailableInterns(String keyword) {
        List<InternProfile> interns = teamInternRepository.searchAvailableInterns(keyword);

        return interns.stream().map(i -> {
            User u = userRepository.findById(i.getUserId()).orElse(null);
            return new InternSearchDTO(
                    i.getInternId(),
                    u.getFullName(),
                    u.getEmail(),
                    u.getPhone(),
                    i.getGpa(),
                    i.getMajor(),
                    i.getSchool()
            );
        }).toList();
    }

    public String addInternToTeam(Integer programId, Integer teamId, Integer internId) {

        var program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        if (!program.getProgramStatus().equals(ProgramStatus.UPCOMING)) {
            throw new RuntimeException("Intern can only be added when program is UPCOMING");
        }

        if (teamInternRepository.isInternInProgram(internId, programId)) {
            throw new RuntimeException("Intern already assigned in this program");
        }

        if (teamInternRepository.isInternInAnyTeam(internId)) {
            throw new RuntimeException("Intern already assigned to another team");
        }

        var team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // ✅ Check that the team belongs to the correct program
        if (!team.getProgram().getProgramId().equals(programId)) {
            throw new RuntimeException("Team does not belong to the specified program");
        }

        var intern = internRepository.findById(internId)
                .orElseThrow(() -> new RuntimeException("Intern not found"));

        TeamIntern teamIntern = new TeamIntern(
                team,
                intern,
                LocalDateTime.now()
        );

        teamInternRepository.save(teamIntern);

        return "Intern added successfully";
    }

    public List<InternDetailDTO> getInternsByTeam(Integer teamId) {

        List<TeamIntern> teamInterns = teamInternRepository.findByTeamTeamId(teamId);

        return teamInterns.stream().map(teamIntern -> {
            InternProfile intern = teamIntern.getIntern();
            User user = userRepository.findById(intern.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found for intern " + intern.getInternId()));

            return new InternDetailDTO(
                    intern.getInternId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone(),
                    intern.getGpa(),
                    intern.getMajor(),
                    intern.getSchool()
            );
        }).collect(Collectors.toList());
    }
}
