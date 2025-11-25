package com.example.Internship_System.program.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ScheduleEventDTO {
    private String id;
    private String title;
    private String description;
    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private String details;

    // Constructor mặc định (bắt buộc để Jackson serialize/deserialize)
    public ScheduleEventDTO() {}

    public ScheduleEventDTO(String id, String title, String description,
                            String type, LocalDateTime date, String details) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.date = date;
        this.details = details;
    }

    // getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public LocalDateTime getDate() { return date; }
    public String getDetails() { return details; }

    // setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public void setDetails(String details) { this.details = details; }
}
