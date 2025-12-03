package com.example.Internship_System.program.dto;

import java.time.LocalDateTime;


public class ProgramUpdateRequest {

    private String name;
    private String department;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String detail;
    private Integer maxInterns;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public Integer getMaxInterns() { return maxInterns; }
    public void setMaxInterns(Integer maxInterns) { this.maxInterns = maxInterns; }
}
