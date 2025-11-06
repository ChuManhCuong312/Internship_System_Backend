package com.example.Internship_System.mentor.controller;

import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.mentor.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MentorController {

    private final MentorService mentorService;

    @GetMapping
    public ResponseEntity<List<MentorUser>> getAllMentors() {
        return ResponseEntity.ok(mentorService.getAllMentors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorUser> getMentor(@PathVariable Long id) {
        return ResponseEntity.ok(mentorService.getMentorById(id));
    }

    @PostMapping
    public ResponseEntity<MentorUser> createMentor(@RequestBody MentorUser mentor) {
        return ResponseEntity.ok(mentorService.createMentor(mentor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MentorUser> updateMentor(
            @PathVariable Long id,
            @RequestBody MentorUser mentor
    ) {
        return ResponseEntity.ok(mentorService.updateMentor(id, mentor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMentor(@PathVariable Long id) {
        mentorService.deleteMentor(id);
        return ResponseEntity.ok("Mentor deleted successfully");
    }
}
