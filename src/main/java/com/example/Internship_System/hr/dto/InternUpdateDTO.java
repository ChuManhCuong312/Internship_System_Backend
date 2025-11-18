package com.example.Internship_System.hr.dto;

import java.time.LocalDate;

public class InternUpdateDTO {
    private String school;
    private String major;
    private LocalDate dob;
    private String address;
    private String gender;
    private double gpa;
    private String phone;

    // Getters & Setters
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
