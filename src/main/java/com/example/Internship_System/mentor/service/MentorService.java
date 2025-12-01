package com.example.Internship_System.mentor.service;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.mentor.dto.MentorDTO;
import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.repository.MentorRepository;
import com.example.Internship_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    public List<MentorDTO> getAllMentors() {
        List<MentorUser> mentors = mentorRepository.findAll();

        return mentors.stream().map(m -> {
            User u = m.getUser(); // JPA already loads user
            return new MentorDTO(
                    m.getMentorId(),
                    u != null ? u.getFullName() : null,
                    u != null ? u.getEmail() : null
            );
        }).toList();
    }

    public MentorDTO getMentorByUserId(Integer userId) {
        MentorUser mentor = mentorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Mentor not found for userId: " + userId));
        User u = mentor.getUser();
        return new MentorDTO(
                mentor.getMentorId(),
                u != null ? u.getFullName() : null,
                u != null ? u.getEmail() : null
        );
    }
}

