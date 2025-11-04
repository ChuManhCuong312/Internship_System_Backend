package com.example.Internship_System.repository;

import com.example.Internship_System.intern.entity.InternProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternRepository extends JpaRepository<InternProfile, Integer> {
    Optional<InternProfile> findByUserId(int userId);

    boolean existsByUserId(int userId);
}