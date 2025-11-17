package com.example.Internship_System.repository;

import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import com.example.Internship_System.hr.dto.CandidateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface HRRepository extends JpaRepository<InternProfile, Integer> {
    @Query("SELECT new com.example.Internship_System.hr.dto.HRInternDTO(" +
            "i.internId, u.userId, u.fullName, u.email, u.phone, " +
            "i.cvPath, i.internshipApplictionPath, i.gpa, i.status, i.major, i.school, " +
            "i.dob, i.address) " +
            "FROM InternProfile i JOIN User u ON i.userId = u.userId " +
            "WHERE (:searchTerm IS NULL OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:major IS NULL OR LOWER(i.major) LIKE LOWER(CONCAT('%', :major, '%'))) " +
            "AND (:status IS NULL OR i.status = :status)")
    Page<HRInternDTO> findAllInternProfilesForHR(
            @Param("searchTerm") String searchTerm,
            @Param("major") String major,
            @Param("status") String status,
            Pageable pageable
    );

    @Query("SELECT new com.example.Internship_System.hr.dto.CandidateDTO(" +
            "u.userId, u.fullName, u.email, u.phone) " +
            "FROM User u WHERE u.role.roleId = 4 " +
            "AND NOT EXISTS (SELECT 1 FROM InternProfile i WHERE i.userId = u.userId)")
    Page<CandidateDTO> findInternUsersWithoutProfile(Pageable pageable);
}

