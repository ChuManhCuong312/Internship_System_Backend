package com.example.Internship_System.mentor.controller;

import com.example.Internship_System.mentor.dto.MentorDTO;
import com.example.Internship_System.mentor.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user/{userId}")
    public ResponseEntity<MentorDTO> getMentorByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(mentorService.getMentorByUserId(userId));
    }
}