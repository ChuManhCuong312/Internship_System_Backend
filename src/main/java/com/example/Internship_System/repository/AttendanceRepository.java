package com.example.Internship_System.repository;

import com.example.Internship_System.intern.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    Optional<Attendance> findByAttendanceId(int attendanceId);
    List<Attendance> findByInternId(int internId);

    @Query("SELECT a FROM Attendance a WHERE a.internId = :internId AND a.date = :date")
    Optional<Attendance> findByInternIdAndDate(@Param("internId") int internId,
                                               @Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.internId = :internId ORDER BY a.date DESC")
    List<Attendance> findAttendanceHistoryByInternId(@Param("internId") int internId);

    @Query("SELECT a FROM Attendance a WHERE a.date = :date")
    List<Attendance> findAllByDate(@Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.internId = :internId " +
            "AND a.date BETWEEN :startDate AND :endDate ORDER BY a.date DESC")
    List<Attendance> findAttendanceByInternIdAndDateRange(
            @Param("internId") int internId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Attendance a WHERE a.internId = :internId AND a.date = :date AND a.checkIn IS NOT NULL")
    boolean hasCheckedInToday(@Param("internId") int internId, @Param("date") LocalDate date);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Attendance a WHERE a.internId = :internId AND a.date = :date AND a.checkOut IS NOT NULL")
    boolean hasCheckedOutToday(@Param("internId") int internId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.internId = :internId AND a.checkIn IS NOT NULL")
    long countWorkingDaysByInternId(@Param("internId") int internId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.internId = :internId " +
            "AND a.checkIn IS NOT NULL AND a.checkIn > :lateTime")
    long countLateDaysByInternId(@Param("internId") int internId,
                                 @Param("lateTime") LocalTime lateTime);

}
