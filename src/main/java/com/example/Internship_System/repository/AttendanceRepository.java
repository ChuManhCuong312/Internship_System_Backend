package com.example.Internship_System.repository;

import com.example.Internship_System.intern.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    Optional<Attendance> findByAttendanceId(int attendanceId);
    List<Attendance> findByInternId(int internId);
}
