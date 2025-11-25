package com.example.Internship_System.program.dto;

import java.time.LocalDate;


public class ProgramUpdateRequest {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String detail;
    private Integer maxInterns;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public Integer getMaxInterns() { return maxInterns; }
    public void setMaxInterns(Integer maxInterns) { this.maxInterns = maxInterns; }
}
