package com.example.Internship_System.intern.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternProfileDTO {
    private int internId;
    private int userId;
    private String fullName;
    private String email;
    private String school;
    private String major;
    private String status;
    private String gender;
    private LocalDateTime createdAt;
    private List<String> documents;



    public InternProfileDTO(int internId, int userId, String fullName, String email,
                            String school, String major, String status, String gender,
                            LocalDateTime createdAt, List<String> documents) {
        this.internId = internId;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.school = school;
        this.major = major;
        this.status = status;
        this.gender = gender;
        this.createdAt = createdAt;
        this.documents = documents;

    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getInternId() { return internId; }
    public void setInternId(int internId) { this.internId = internId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<String> getDocuments() { return documents; }
    public void setDocuments(List<String> documents) { this.documents = documents; }

}
