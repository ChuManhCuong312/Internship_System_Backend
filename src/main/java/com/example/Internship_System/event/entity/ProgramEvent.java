package com.example.Internship_System.event.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "program_events")
public class ProgramEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer eventId;

    @Column(name = "program_id", nullable = false)
    private Integer programId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 200)
    private String location;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(columnDefinition = "TEXT")
    private String description;

    /* ================= CONSTRUCTORS ================= */

    // Constructor mặc định (bắt buộc cho JPA)
    public ProgramEvent() {
    }

    // Constructor không có eventId (dùng khi tạo mới)
    public ProgramEvent(Integer programId, String title, String location,
                        LocalDate eventDate, LocalTime startTime,
                        LocalTime endTime, String description) {
        this.programId = programId;
        this.title = title;
        this.location = location;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    // Constructor đầy đủ
    public ProgramEvent(Integer eventId, Integer programId, String title, String location,
                        LocalDate eventDate, LocalTime startTime,
                        LocalTime endTime, String description) {
        this.eventId = eventId;
        this.programId = programId;
        this.title = title;
        this.location = location;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    /* ================= GETTERS & SETTERS ================= */

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
