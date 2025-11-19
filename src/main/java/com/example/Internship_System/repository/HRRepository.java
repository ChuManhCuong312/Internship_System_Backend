package com.example.Internship_System.repository;

import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import com.example.Internship_System.hr.dto.CandidateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface HRRepository extends JpaRepository<InternProfile, Integer> {
    @Query("SELECT new com.example.Internship_System.hr.dto.HRInternDTO(" +
            "i.internId, u.userId, u.fullName, u.email, u.phone, " +
            "i.cvPath, i.permissionFile, i.gpa, i.status, i.major, i.school, " +
            "i.dob, i.address, i.gender) " +
            "FROM InternProfile i JOIN User u ON i.userId = u.userId " +
            "WHERE (:searchTerm IS NULL OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:major IS NULL OR LOWER(i.major) LIKE LOWER(CONCAT('%', :major, '%'))) " +
            "AND (:school IS NULL OR LOWER(i.school) = LOWER(:school)) " +
            "AND (:status IS NULL OR i.status = :status)" +
            "ORDER BY i.internId DESC")
    Page<HRInternDTO> findAllInternProfilesForHR(
            @Param("searchTerm") String searchTerm,
            @Param("major") String major,
            @Param("school") String school,
            @Param("status") String status,
            Pageable pageable
    );

    @Query("SELECT new com.example.Internship_System.hr.dto.CandidateDTO(" +
            "u.userId, u.fullName, u.email, u.phone) " +
            "FROM User u WHERE u.role.roleId = 4 " +
            "AND NOT EXISTS (SELECT 1 FROM InternProfile i WHERE i.userId = u.userId)")
    Page<CandidateDTO> findInternUsersWithoutProfile(Pageable pageable);

    @Query("SELECT DISTINCT i.major FROM InternProfile i WHERE i.major IS NOT NULL ORDER BY i.major ASC")
    List<String> findDistinctMajors();
    @Query("SELECT DISTINCT i.school FROM InternProfile i WHERE i.school IS NOT NULL ORDER BY i.school ASC")
    List<String> findDistinctSchools();

}

