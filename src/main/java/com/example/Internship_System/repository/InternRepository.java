package com.example.Internship_System.repository;

import com.example.Internship_System.hr.dto.InternAssignmentViewDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.intern.dto.InternProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT new com.example.Internship_System.intern.dto.InternProfileDTO(" +
            "i.internId, i.userId, u.fullName, u.email, i.school, i.major, i.status, " +
            "i.gender, u.createdAt, NULL) " +
            "FROM InternProfile i JOIN User u ON i.userId = u.userId " +
            "WHERE (:searchTerm IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:major IS NULL OR LOWER(i.major) LIKE LOWER(CONCAT('%', :major, '%'))) " +
            "AND (:status IS NULL OR i.status = :status)")
    List<InternProfileDTO> searchInterns(@Param("searchTerm") String searchTerm,
                                         @Param("major") String major,
                                         @Param("status") String status);

    @Query("SELECT DISTINCT i.major FROM InternProfile i ORDER BY i.major")
    List<String> findDistinctMajors();


    @Query("SELECT new com.example.Internship_System.hr.dto.InternAssignmentViewDTO(" +
            "i.internId, u.fullName, cd.internConfirmStatus, m.mentorId, mu.fullName, ma.assignedAt) " +
            "FROM InternProfile i " +
            "LEFT JOIN User u ON i.userId = u.userId " +
            "LEFT JOIN ContractDocument cd ON cd.intern = i " +
            "LEFT JOIN MentorAssignment ma ON ma.intern = i " +
            "LEFT JOIN MentorUser m ON ma.mentor = m " +
            "LEFT JOIN User mu ON m.user = mu " +
            "WHERE (:searchTerm IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(mu.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:filter = 'all' OR " +
            "(:filter = 'withMentor' AND m.mentorId IS NOT NULL) OR " +
            "(:filter = 'withoutMentor' AND m.mentorId IS NULL) OR " +
            "(:filter = 'unapproved' AND cd.internConfirmStatus <> 'APPROVED'))")
    Page<InternAssignmentViewDTO> findInternsWithAssignments(@Param("searchTerm") String searchTerm,
                                                             @Param("filter") String filter,
                                                             Pageable pageable);

}
