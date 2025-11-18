package com.example.Internship_System.repository;

import com.example.Internship_System.mentor.entity.MentorUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<MentorUser, Integer> {
    @Query("SELECT m from MentorUser m JOIN FETCH m.user")
    List<MentorUser> findAllWithUser();
}
