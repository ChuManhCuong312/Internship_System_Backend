package com.example.Internship_System.intern.mapper;

import com.example.Internship_System.intern.dto.InternProfileDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.auth.entity.User;

import java.util.ArrayList;
import java.util.List;

public class InternProfileMapper {
    public static InternProfileDTO toDTO(InternProfile intern, User user) {
        List<String> documents = new ArrayList<>();
        if (intern.getCvPath() != null && !intern.getCvPath().isEmpty()) {
            documents.add(intern.getCvPath());
        }

        return new InternProfileDTO(
                intern.getInternId(),
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                intern.getSchool(),
                intern.getMajor(),
                intern.getStatus(),
                intern.getGender(),
                user.getCreatedAt(),  // Pass LocalDateTime directly, not String
                documents
        );
    }
}