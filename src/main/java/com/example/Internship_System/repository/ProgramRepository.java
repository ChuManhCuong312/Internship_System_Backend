package com.example.Internship_System.repository;

import com.example.Internship_System.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Integer> {

    // Search by name (contains, ignore case)
    @Query("SELECT p FROM Program p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Program> searchByName(String name);

    List<Program> findByDepartmentIgnoreCase(String department);

    @Query("""
    SELECT p FROM Program p
    JOIN Team t ON t.program = p
    JOIN TeamIntern ti ON ti.team = t
    WHERE ti.intern.internId = :internId
""")
    Optional<Program> findProgramByInternId(Integer internId);

    @Query("SELECT DISTINCT p.department FROM Program p")
    List<String> findDistinctDepartments();
    @Query("""
        SELECT p
        FROM Program p
        JOIN MentorProgram mp ON mp.program = p
        JOIN mp.mentor m
        JOIN m.user u
        WHERE u.userId = :userId
          AND p.programStatus = 'ON_GOING'
    """)
    List<Program> findOngoingProgramsByMentorUserId(Integer userId);


}
