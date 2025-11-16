package com.example.Internship_System.repository;

import com.example.Internship_System.hr.entity.MentorAssignment;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.mentor.entity.MentorUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorAssignmentRepository extends JpaRepository<MentorAssignment, Integer> {
    List<MentorAssignment> findByMentor(MentorUser mentor);

    Optional<MentorAssignment> findByIntern(InternProfile intern);

    boolean existsByIntern(InternProfile intern);

    boolean existsByMentor(MentorUser mentor);
}