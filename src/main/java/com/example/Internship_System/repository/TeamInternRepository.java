package com.example.Internship_System.repository;

import com.example.Internship_System.team.entity.TeamIntern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamInternRepository extends JpaRepository<TeamIntern, Integer> {

    @Query("SELECT COUNT(ti) FROM TeamIntern ti WHERE ti.team.program.programId = :programId")
    int countInterns(@Param("programId") Integer programId);

    List<TeamIntern> findByTeamTeamId(Integer teamId);
}
