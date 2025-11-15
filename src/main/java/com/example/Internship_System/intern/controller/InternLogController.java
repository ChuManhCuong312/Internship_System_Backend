package com.example.Internship_System.intern.controller;

import com.example.Internship_System.intern.entity.InternLog;
import com.example.Internship_System.repository.InternLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/intern/logs")
public class InternLogController {

    @Autowired
    private InternLogRepository logRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<InternLog>> getLogs(@PathVariable int userId) {
        List<InternLog> logs = logRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(logs);
    }
}

