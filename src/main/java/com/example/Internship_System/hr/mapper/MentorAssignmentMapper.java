package com.example.Internship_System.hr.mapper;

import com.example.Internship_System.hr.dto.MentorAssignmentDTO;
import com.example.Internship_System.hr.entity.MentorAssignment;

public class MentorAssignmentMapper {
    public static MentorAssignmentDTO toDTO(MentorAssignment entity) {
        if (entity == null) return null;

        String mentorName = entity.getMentor() != null && entity.getMentor().getUser() != null
                ? entity.getMentor().getUser().getFullName()
                : null;

        String internName = entity.getIntern() != null && entity.getIntern().getUser() != null
                ? entity.getIntern().getUser().getFullName()
                : null;

        return new MentorAssignmentDTO(
                entity.getAssignmentId(),
                mentorName,
                internName,
                entity.getAssignedAt()
        );
    }
}
