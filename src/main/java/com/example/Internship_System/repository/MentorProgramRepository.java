package com.example.Internship_System.repository;

import com.example.Internship_System.program.entity.MentorProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MentorProgramRepository extends JpaRepository<MentorProgram, Integer> {

    // Sort by mentor full name
    List<MentorProgram> findByMentor_MentorId(Integer mentorId);
}