package com.example.Internship_System.repository;

import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface HRRepository extends JpaRepository<InternProfile, Integer> {

    @Query("SELECT new com.example.Internship_System.hr.dto.HRInternDTO(" +
            "i.internId, u.userId, u.fullName, u.email, u.phone, " +
            "i.cvPath, i.gpa, i.cvFile, i.status, i.major, i.school) " +
            "FROM InternProfile i JOIN User u ON i.userId = u.userId")
    List<HRInternDTO> findAllInternProfilesForHR();
    @Query("SELECT new com.example.Internship_System.hr.dto.HRInternDTO(" +
            "i.internId, u.userId, u.fullName, u.email, u.phone, " +
            "i.cvPath, i.gpa, i.cvFile, i.status, i.major, i.school) " +
            "FROM InternProfile i JOIN User u ON i.userId = u.userId " +
            "WHERE ((:searchTerm IS NULL) OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:major IS NULL OR LOWER(i.major) LIKE LOWER(CONCAT('%', :major, '%'))) " +
            "AND (:status IS NULL OR i.status = :status)")
    List<HRInternDTO> searchInternProfilesForHR(
            @Param("searchTerm") String searchTerm,
            @Param("major") String major,
            @Param("status") String status
    );
}

