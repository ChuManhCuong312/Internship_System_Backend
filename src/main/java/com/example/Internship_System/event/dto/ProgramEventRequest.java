package com.example.Internship_System.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class ProgramEventRequest {

    @NotNull
    private Integer programId;

    @NotBlank
    private String title;

    private String location;

    @NotNull
    private LocalDate eventDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private String description;

    /* ================= CONSTRUCTORS ================= */

    // Constructor mặc định
    public ProgramEventRequest() {
    }

    // Constructor đầy đủ (không có eventId vì là request)
    public ProgramEventRequest(Integer programId, String title, String location,
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

    /* ================= GETTERS & SETTERS ================= */

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
