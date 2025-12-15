package com.example.Internship_System.event.repository;

import com.example.Internship_System.event.entity.ProgramEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ProgramEventRepository extends JpaRepository<ProgramEvent, Integer> {
    List<ProgramEvent> findByProgramId(Integer programId);
    @Query("""
    SELECT e FROM ProgramEvent e
    WHERE e.programId = :programId
      AND e.eventDate = :eventDate
      AND e.startTime < :endTime
      AND e.endTime > :startTime
    """)
    List<ProgramEvent> findOverlappingEvents(
            @Param("programId") Integer programId,
            @Param("eventDate") LocalDate eventDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
    @Query("""
    SELECT e FROM ProgramEvent e
    WHERE e.programId = :programId
      AND e.eventDate = :eventDate
      AND e.startTime < :endTime
      AND e.endTime > :startTime
      AND e.eventId <> :eventId
    """)
    List<ProgramEvent> findOverlappingEventsExcludeSelf(
            @Param("eventId") Integer eventId,
            @Param("programId") Integer programId,
            @Param("eventDate") LocalDate eventDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
    boolean existsByProgramIdAndTitle(Integer programId, String title);

    boolean existsByProgramIdAndTitleAndEventIdNot(
            Integer programId,
            String title,
            Integer eventId
    );


}

