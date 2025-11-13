package com.example.Internship_System.repository;

import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HRRepository extends JpaRepository<InternProfile, Integer> {

    @Query("SELECT new com.example.Internship_System.hr.dto.HRInternDTO(" +
            "i.internId, u.userId, u.fullName, u.email, u.phone, " +
            "i.cvPath, i.gpa, i.cvFile, i.status, i.major) " +
            "FROM InternProfile i JOIN User u ON i.userId = u.userId")
    List<HRInternDTO> findAllInternProfilesForHR();
}
