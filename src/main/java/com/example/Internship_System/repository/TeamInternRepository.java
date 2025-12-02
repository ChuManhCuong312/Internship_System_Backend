package com.example.Internship_System.repository;

import com.example.Internship_System.team.entity.TeamIntern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamInternRepository extends JpaRepository<TeamIntern, Integer> {

    @Query("SELECT COUNT(ti) FROM TeamIntern ti WHERE ti.team.program.programId = :programId")
    int countInterns(@Param("programId") Integer programId);

    List<TeamIntern> findByTeamTeamId(Integer teamId);

    void deleteByTeam_TeamIdAndIntern_InternId(Integer teamId, Integer internId);

    void deleteAllByTeam_TeamId(Integer teamId);
    Optional<TeamIntern> findByIntern_InternId(Integer internId);
    @Query("SELECT ti.team.teamId FROM TeamIntern ti WHERE ti.intern.internId = :internId")
    Integer findTeamIdByInternId(@Param("internId") Integer internId);
    @Query("SELECT ti FROM TeamIntern ti WHERE ti.intern.internId = :internId")
    TeamIntern findByInternId(@Param("internId") Integer internId);
    @Query("SELECT COUNT(ti) FROM TeamIntern ti WHERE ti.team.teamId = :teamId")
    int countByTeamId(@Param("teamId") Integer teamId);
    List<TeamIntern> findByTeam_TeamId(Integer teamId);
}
