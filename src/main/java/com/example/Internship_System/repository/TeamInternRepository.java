package com.example.Internship_System.repository;

import com.example.Internship_System.intern.entity.InternProfile;
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

    boolean existsByTeamTeamIdAndInternInternId(Integer teamId, Integer internId);

    @Query("""
    SELECT i
    FROM InternProfile i
    JOIN ContractDocument c ON c.intern.internId = i.internId
    LEFT JOIN TeamIntern ti ON ti.intern.internId = i.internId
    JOIN User u ON i.userId = u.userId
    WHERE u.fullName LIKE %:keyword%
      AND c.internConfirmStatus = com.example.Internship_System.intern.entity.InternConfirmStatus.APPROVED
      AND u.status = com.example.Internship_System.auth.entity.UserStatus.ACTIVE
      AND ti.team IS NULL
""")
    List<InternProfile> searchAvailableInterns(@Param("keyword") String keyword);

    @Query("""
    SELECT COUNT(ti) > 0
    FROM TeamIntern ti
    JOIN ti.team t
    WHERE ti.intern.internId = :internId
      AND t.program.programId = :programId
""")
    boolean isInternInProgram(int internId, int programId);

    @Query("""
    SELECT COUNT(ti) > 0
    FROM TeamIntern ti
    WHERE ti.intern.internId = :internId
""")
    boolean isInternInAnyTeam(int internId);
}
