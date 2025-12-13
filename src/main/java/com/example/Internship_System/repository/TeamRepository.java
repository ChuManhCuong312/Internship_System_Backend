package com.example.Internship_System.repository;

import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    List<Team> findByProgramProgramId(Integer programId);

    @Query("SELECT COUNT(t) FROM Team t WHERE t.program.programId = :programId")
    int countTeams(@Param("programId") Integer programId);

    @Query("SELECT t.mentor FROM Team t WHERE t.program.programId = :programId")
    List<MentorUser> findMentorsByProgram(@Param("programId") Integer programId);

    boolean existsByProgramProgramIdAndMentorMentorId(Integer programId, Integer mentorId);

    boolean existsByTeamIdAndProgram_ProgramId(Integer teamId, Integer programId);

    List<Team> findByProgramProgramIdOrderByTeamIdAsc(Integer programId);
}
