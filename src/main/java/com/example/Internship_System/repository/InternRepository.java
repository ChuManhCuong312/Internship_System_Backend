package com.example.Internship_System.repository;

import com.example.Internship_System.intern.entity.InternProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternRepository extends JpaRepository<InternProfile, Integer> {

    Optional<InternProfile> findByUserId(int userId);

    List<InternProfile> findByStatus(String status);

    List<InternProfile> findByMajorContainingIgnoreCase(String major);

    List<InternProfile> findByMajorContainingIgnoreCaseAndStatus(
            String major, String status);

    @Query("SELECT i FROM InternProfile i JOIN User u ON i.userId = u.userId " +
            "WHERE (:searchTerm IS NULL OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:major IS NULL OR LOWER(i.major) LIKE LOWER(CONCAT('%', :major, '%'))) " +
            "AND (:status IS NULL OR i.status = :status)")
    List<InternProfile> searchInterns(
            @Param("searchTerm") String searchTerm,
            @Param("major") String major,
            @Param("status") String status
    );

    @Query("SELECT DISTINCT i.major FROM InternProfile i ORDER BY i.major")
    List<String> findDistinctMajors();
}