package com.example.Internship_System.hr.service;

import com.example.Internship_System.hr.dto.InternAssignmentViewDTO;
import com.example.Internship_System.hr.dto.MentorViewDTO;
import com.example.Internship_System.hr.entity.MentorAssignment;
import com.example.Internship_System.intern.entity.ContractDocument;
import com.example.Internship_System.intern.entity.InternConfirmStatus;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MentorAssignmentService {

    private final MentorAssignmentRepository mentorAssignmentRepository;
    private final InternRepository internRepository;
    private final MentorRepository mentorRepository;
    private final ContractDocumentRepository contractDocumentRepository;

    public MentorAssignmentService(
            MentorAssignmentRepository mentorAssignmentRepository,
            InternRepository internRepository,
            MentorRepository mentorRepository,
            UserRepository userRepository,
            ContractDocumentRepository contractDocumentRepository) {
        this.mentorAssignmentRepository = mentorAssignmentRepository;
        this.internRepository = internRepository;
        this.mentorRepository = mentorRepository;
        this.contractDocumentRepository = contractDocumentRepository;
    }

    public List<MentorAssignment> getAllAssignments() {
        return mentorAssignmentRepository.findAll();
    }

    public List<InternProfile> getInternsWithoutMentor() {
        List<InternProfile> interns = internRepository.findAll();

        return interns.stream()
                .filter(intern -> !mentorAssignmentRepository.existsByIntern(intern))
                .toList();
    }

    public MentorAssignment assignMentor(Integer internId, Integer mentorId) {

        InternProfile intern = internRepository.findById(internId)
                .orElseThrow(() -> new RuntimeException("Intern not found"));

        MentorUser mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        if (mentorAssignmentRepository.existsByIntern(intern)) {
            throw new RuntimeException("This intern already has a mentor assigned");
        }

        // --- SỬA LỖI TẠI ĐÂY: Xử lý List thay vì Optional ---
        List<ContractDocument> contracts = contractDocumentRepository.findByIntern(intern);
        
        if (contracts.isEmpty()) {
            throw new RuntimeException("Intern does not have a contract document");
        }

        // Kiểm tra xem có bất kỳ hợp đồng nào đã được APPROVED không
        boolean hasApprovedContract = contracts.stream()
                .anyMatch(c -> c.getInternConfirmStatus() == InternConfirmStatus.APPROVED);

        if (!hasApprovedContract) {
            throw new RuntimeException("Intern contract is NOT approved yet");
        }
        // ----------------------------------------------------

        MentorAssignment assignment = new MentorAssignment(mentor, intern);

        return mentorAssignmentRepository.save(assignment);
    }


    public Page<InternAssignmentViewDTO> listInternsWithAssignments(String searchTerm, String filter, Pageable pageable) {
        return internRepository.findInternsWithAssignments(searchTerm, filter, pageable);
    }

    public MentorAssignment reassignMentor(Integer internId, Integer mentorId) {

        InternProfile intern = internRepository.findById(internId)
                .orElseThrow(() -> new RuntimeException("Intern not found"));

        MentorUser mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        // Find existing assignment
        MentorAssignment existingAssignment = mentorAssignmentRepository.findByIntern(intern)
                .orElseThrow(() -> new RuntimeException("No existing mentor assignment to reassign"));

        // --- SỬA LỖI TẠI ĐÂY: Xử lý List thay vì Optional ---
        List<ContractDocument> contracts = contractDocumentRepository.findByIntern(intern);
        
        // Kiểm tra có hợp đồng nào Approved không
        boolean hasApprovedContract = contracts.stream()
                .anyMatch(c -> c.getInternConfirmStatus() == InternConfirmStatus.APPROVED);

        if (contracts.isEmpty() || !hasApprovedContract) {
            throw new RuntimeException("Intern contract is not approved");
        }
        // ----------------------------------------------------

        // Update mentor
        existingAssignment.setMentor(mentor);

        return mentorAssignmentRepository.save(existingAssignment);
    }

    public List<MentorViewDTO> getMentorsWithInternCount(){
        List<MentorUser> mentors = mentorRepository.findAllWithUser();

        List<Object[]> counts = mentorAssignmentRepository.countAssignedInterns();

        Map<Integer,Integer> countMap = new HashMap<>();
        for(Object[] row : counts){
            Integer mentorId = ((Number) row[0]).intValue();
            Integer count = ((Number) row[1]).intValue();
            countMap.put(mentorId,count);
        }

        List<MentorViewDTO> result = new ArrayList<>();
        for(MentorUser mentor : mentors){
            Integer mentorId = mentor.getMentorId();
            String name = mentor.getUser().getFullName();
            Integer assigned = countMap.getOrDefault(mentorId, 0);

            result.add(new MentorViewDTO(mentorId, name, assigned));
        }
        return result;
    }
}