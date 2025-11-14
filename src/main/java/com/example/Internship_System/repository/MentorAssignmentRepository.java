package com.example.Internship_System.repository;

import com.example.Internship_System.hr.entity.MentorAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorAssignmentRepository extends JpaRepository<MentorAssignment, Integer> {
}
