package com.example.Internship_System.mentor.service;

import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    public List<MentorUser> getAllMentors() {
        return mentorRepository.findAll();
    }

    public MentorUser getMentorById(Long id) {
        return mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor not found with id: " + id));
    }

    public MentorUser createMentor(MentorUser mentor) {
        return mentorRepository.save(mentor);
    }

    public MentorUser updateMentor(Long id, MentorUser updatedMentor) {
        MentorUser existing = getMentorById(id);
        existing.setDepartment(updatedMentor.getDepartment());
        existing.setExpertise(updatedMentor.getExpertise());
        if (updatedMentor.getUser() != null)
            existing.setUser(updatedMentor.getUser());
        return mentorRepository.save(existing);
    }

    public void deleteMentor(Long id) {
        if (!mentorRepository.existsById(id))
            throw new RuntimeException("Mentor not found");
        mentorRepository.deleteById(id);
    }
}
