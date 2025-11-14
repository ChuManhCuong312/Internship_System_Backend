package com.example.Internship_System.repository;

import com.example.Internship_System.mentor.entity.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<MentorProfile, Integer> {
    Optional<MentorProfile> findByUser_UserId(Integer userId);
}
