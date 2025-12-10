package com.example.Internship_System.repository;

import com.example.Internship_System.task.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByProgramId(Integer programId);
    Optional<Tag> findByNameAndProgramId(String name, Integer programId);
    boolean existsByNameAndProgramId(String name, Integer programId);
}
