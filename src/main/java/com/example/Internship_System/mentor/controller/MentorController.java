package com.example.Internship_System.mentor.controller;

import com.example.Internship_System.mentor.dto.MentorDTO;
import com.example.Internship_System.mentor.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @GetMapping
    public ResponseEntity<List<MentorDTO>> getAllMentors() {
        return ResponseEntity.ok(mentorService.getAllMentors());
    }
}