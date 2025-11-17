package com.example.Internship_System.hr.mapper;

import com.example.Internship_System.hr.dto.MentorAssignmentDTO;
import com.example.Internship_System.hr.entity.MentorAssignment;

public class MentorAssignmentMapper {
    public static MentorAssignmentDTO toDTO(MentorAssignment assignment) {
        return new MentorAssignmentDTO(
                assignment.getAssignmentId(),
                assignment.getIntern().getInternId(),
                assignment.getIntern().getSchool(), // or fullName if available
                assignment.getMentor().getMentorId(),
                assignment.getMentor().getUser().getFullName(),
                assignment.getAssignedAt()
        );
    }
}
