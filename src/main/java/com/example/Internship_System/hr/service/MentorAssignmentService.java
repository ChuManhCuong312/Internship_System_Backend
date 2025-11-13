package com.example.Internship_System.hr.service;
import com.example.Internship_System.hr.dto.MentorAssignmentDTO;
import com.example.Internship_System.hr.dto.MentorAssignmentRequestDTO;
import com.example.Internship_System.hr.entity.MentorAssignment;
import com.example.Internship_System.hr.mapper.MentorAssignmentMapper;
import com.example.Internship_System.repository.InternRepository;
import com.example.Internship_System.repository.MentorAssignmentRepository;
import com.example.Internship_System.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MentorAssignmentService {

    @Autowired
    private MentorAssignmentRepository mentorAssignmentRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private InternRepository internRepository;

    public List<MentorAssignmentDTO> getAllAssignments() {
        return mentorAssignmentRepository.findAll()
                .stream()
                .map(MentorAssignmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<MentorAssignmentDTO> getAssignmentById(Integer id) {
        return mentorAssignmentRepository.findById(id)
                .map(MentorAssignmentMapper::toDTO);
    }

    public MentorAssignmentDTO createAssignment(MentorAssignmentRequestDTO request) {
        var mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        var intern = internRepository.findById(request.getInternId())
                .orElseThrow(() -> new RuntimeException("Intern not found"));

        MentorAssignment assignment = new MentorAssignment();
        assignment.setMentor(mentor);
        assignment.setIntern(intern);
        assignment.setAssignedAt(java.time.LocalDateTime.now());

        MentorAssignment saved = mentorAssignmentRepository.save(assignment);
        return MentorAssignmentMapper.toDTO(saved);
    }

    public MentorAssignmentDTO updateAssignment(Integer id, MentorAssignmentRequestDTO request) {
        MentorAssignment saved = mentorAssignmentRepository.findById(id)
                .map(existing -> {
                    var mentor = mentorRepository.findById(request.getMentorId())
                            .orElseThrow(() -> new RuntimeException("Mentor not found"));
                    var intern = internRepository.findById(request.getInternId())
                            .orElseThrow(() -> new RuntimeException("Intern not found"));

                    existing.setMentor(mentor);
                    existing.setIntern(intern);
                    existing.setAssignedAt(java.time.LocalDateTime.now());

                    return mentorAssignmentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        return MentorAssignmentMapper.toDTO(saved);
    }
    public void deleteAssignment(Integer id) {
        mentorAssignmentRepository.deleteById(id);
    }
}
