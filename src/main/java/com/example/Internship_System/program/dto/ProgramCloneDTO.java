package com.example.Internship_System.program.dto;

import lombok.Data;

@Data
public class ProgramCloneDTO {
    private String name;
    private String department;
    private String details;
    private Integer maxInterns;

    public ProgramCloneDTO() {}
    public ProgramCloneDTO(String name, String department, String details,Integer maxInterns){
        this.name = name;
        this.department = department;
        this.details = details;
        this.maxInterns = maxInterns;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}

    public String getDetails() {return details;}
    public void setDetails(String details) {this.details = details;}

    public Integer getMaxInterns() {return maxInterns;}
    public void setMaxInterns(Integer maxInterns) {this.maxInterns = maxInterns;}
}