package com.example.Internship_System.auth.dto;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;

    // For intern registration (optional fields)
    private String school;
    private String major;

    // Getters and Setters
    public String getFullName() {return fullName;}
    public void setFullName(String fullName) {this.fullName = fullName;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getSchool() {return school;}
    public void setSchool(String school) {this.school = school;}

    public String getMajor() {return major;}
    public void setMajor(String major) {this.major = major;}
}
