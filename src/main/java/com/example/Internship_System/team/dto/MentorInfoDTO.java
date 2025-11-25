package com.example.Internship_System.team.dto;

public class MentorInfoDTO {
    private String name;
    private String email;
    private String phone;
    private String department;
    private String expertise;

    public MentorInfoDTO(String name, String email, String phone, String department, String expertise) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.expertise = expertise;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}

    public String getExpertise() {return expertise;}
    public void setExpertise(String expertise) {this.expertise = expertise;}
}
