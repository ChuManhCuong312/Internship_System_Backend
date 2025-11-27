package com.example.Internship_System.program.entity;

import com.example.Internship_System.auth.entity.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "programs")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Integer programId;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "program_status", columnDefinition = "ENUM('UPCOMING','ON_GOING','FINISHED') DEFAULT 'UPCOMING'")
    private ProgramStatus programStatus = ProgramStatus.UPCOMING;

    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;

    @Column(name = "max_interns")
    private Integer maxInterns;

    public Program(){}

    public Program(String name, String department, LocalDateTime startDate,
                   LocalDateTime endDate, String detail, Integer maxInterns){
        this.name = name;
        this.department = department;
        this.startDate = startDate;
        this.endDate = endDate;
        this.detail = detail;
        this.maxInterns = maxInterns;
    }

    public Integer getProgramId() {return programId;}
    public void setProgramId(Integer programId) {this.programId = programId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}

    public LocalDateTime getStartDate() {return startDate;}
    public void setStartDate(LocalDateTime startDate) {this.startDate = startDate;}

    public LocalDateTime getEndDate() {return endDate;}
    public void setEndDate(LocalDateTime endDate) {this.endDate = endDate;}

    public ProgramStatus getProgramStatus() {return programStatus;}
    public void setProgramStatus(ProgramStatus programStatus) {this.programStatus = programStatus;}

    public String getDetail() {return detail;}
    public void setDetail(String detail) {this.detail = detail;}

    public Integer getMaxInterns() {return maxInterns;}
    public void setMaxInterns(Integer maxInterns) {this.maxInterns = maxInterns;}
}
