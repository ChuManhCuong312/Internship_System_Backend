package com.example.Internship_System.repository;

import com.example.Internship_System.mentor.entity.MentorUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<MentorUser, Integer> {

}
