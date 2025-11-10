package com.example.Internship_System.intern.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class InternProfileDTO {
    private int internId;
    private int userId;
    private String fullName;
    private String email;
    private String school;
    private String major;
    private String status;
    private LocalDateTime createdAt;
    private List<String> documents;
    public InternProfileDTO(int internId,
                            int userId,
                            String fullName,
                            String email,
                            String school,
                            String major,
                            String status) {
        this.internId = internId;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.school = school;
        this.major = major;
        this.status = status;
    }

}
