package com.example.Internship_System.repository;

import com.example.Internship_System.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Integer> {

    // Search by name (contains, ignore case)
    @Query("SELECT p FROM Program p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Program> searchByName(String name);

    List<Program> findByDepartmentIgnoreCase(String department);
}
