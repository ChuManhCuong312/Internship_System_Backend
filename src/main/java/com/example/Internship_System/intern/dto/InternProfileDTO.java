package com.example.Internship_System.intern.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
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
    private String phone;
    private LocalDateTime createdAt;
    private List<String> documents;

public InternProfileDTO(){}

    public InternProfileDTO(int internId, int userId, String fullName, String email,
                            String school, String major, String status, String gender, String phone,
                            LocalDateTime createdAt, List<String> documents) {
        this.internId = internId;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.school = school;
        this.major = major;
        this.status = status;
        this.gender = gender;
        this.phone = phone;
        this.createdAt = createdAt;
        this.documents = documents;

    }

}
