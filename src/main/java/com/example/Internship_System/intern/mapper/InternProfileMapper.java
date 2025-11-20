package com.example.Internship_System.intern.mapper;

import com.example.Internship_System.intern.dto.InternProfileDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.auth.entity.User;

import java.util.ArrayList;
import java.util.List;

public class InternProfileMapper {


    public static InternProfileDTO toDTO(InternProfile intern, User user) {
        List<String> documents = new ArrayList<>();
        if (intern.getCvFile() != null && !intern.getCvFile().isEmpty()) {
            documents.add(intern.getCvFile());
        }
        if (intern.getPermissionFile() != null && !intern.getPermissionFile().isEmpty()) {
            documents.add(intern.getPermissionFile());
        }
        if (intern.getUniversityConfirm() != null && !intern.getUniversityConfirm().isEmpty()) {
            documents.add(intern.getUniversityConfirm());
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
                user.getPhone(),
                user.getCreatedAt(),  // Pass LocalDateTime directly, not String
                documents
        );
    }
}