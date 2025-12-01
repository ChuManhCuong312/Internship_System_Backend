package com.example.Internship_System.repository;

import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.program.entity.MentorProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MentorProgramRepository extends JpaRepository<MentorProgram, Integer> {

    // Sort by mentor full name
    List<MentorProgram> findByMentor_MentorId(Integer mentorId);

    boolean existsByProgram_ProgramIdAndMentor_MentorId(Integer programId, Integer mentorId);

    void deleteByProgram_ProgramIdAndMentor_MentorId(Integer programId, Integer mentorId);
    void deleteByProgram_ProgramId(Integer programId);

    @Query("SELECT DISTINCT mp.mentor FROM MentorProgram mp")
    List<MentorUser> findDistinctAssignedMentors();

    List<MentorProgram> findByProgram_ProgramId(Integer programId);

    // Search mentors by name within a program
    @Query("""
        SELECT mp FROM MentorProgram mp
        WHERE mp.program.programId = :programId
          AND LOWER(mp.mentor.user.fullName) LIKE LOWER(CONCAT('%', :namePart, '%'))
    """)
    List<MentorProgram> searchMentorsInProgram(@Param("programId") Integer programId,
                                               @Param("namePart") String namePart);

}