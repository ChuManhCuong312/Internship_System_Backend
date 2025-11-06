package com.example.Internship_System.repository;

import com.example.Internship_System.intern.entity.Intern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InternRepository extends JpaRepository<Intern, Integer> {
    Optional<Intern> findByUserId(int userId);

    List<Intern> findByStatus(String status);
}