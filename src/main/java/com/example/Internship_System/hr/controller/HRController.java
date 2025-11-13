package com.example.Internship_System.hr.controller;

import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.hr.service.HRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/interns")
@CrossOrigin(origins = "*")
public class HRController {

    @Autowired
    private HRService hrService;

    @GetMapping
    public ResponseEntity<List<HRInternDTO>> getAllInternProfilesForHR() {
        try {
            List<HRInternDTO> profiles = hrService.getAllInternsForHR();
            if (profiles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(profiles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
