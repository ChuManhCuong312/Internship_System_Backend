package com.example.Internship_System.support.controller;

import com.example.Internship_System.support.dto.SupportDTO;
import com.example.Internship_System.support.dto.SupportRequestDTO;
import com.example.Internship_System.support.entity.SupportRequest;
import com.example.Internship_System.support.entity.SupportRequestHistory;
import com.example.Internship_System.support.entity.SupportStatus;
import com.example.Internship_System.support.entity.SupportType;
import com.example.Internship_System.support.service.SupportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support-requests")
public class SupportController {

    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @GetMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> getAll() {
        try {
            List<SupportRequest> list = supportService.getAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR','INTERN')")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            SupportRequest req = supportService.getById(id);
            return ResponseEntity.ok(req);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> filter(@RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer internId,
            @RequestParam(required = false) String keyword) {
        try {
            SupportStatus st = status != null && !status.isEmpty() ? SupportStatus.valueOf(status.toUpperCase()) : null;
            SupportType tp = type != null && !type.isEmpty() ? SupportType.valueOf(type.toUpperCase()) : null;
            List<SupportRequestDTO> list = supportService.filter(st, tp, internId, keyword);
            return ResponseEntity.ok(list);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tham số lọc không hợp lệ"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('INTERN')")
    public ResponseEntity<?> create(@Valid @RequestBody SupportDTO dto,
            @RequestParam Integer internId) {
        try {
            SupportRequest created = supportService.createSupportRequest(dto, internId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('INTERN')")
    public ResponseEntity<?> myRequests(@RequestParam Integer internId) {
        try {
            List<SupportRequest> list = supportService.getMyRequests(internId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> approve(@PathVariable Integer id,
            @RequestParam Integer hrId,
            @RequestParam(required = false) String response) {
        try {
            SupportRequest updated = supportService.approve(id, hrId, response);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> reject(@PathVariable Integer id,
            @RequestParam Integer hrId,
            @RequestParam String response) {
        try {
            SupportRequest updated = supportService.reject(id, hrId, response);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('HR','INTERN')")
    public ResponseEntity<?> history(@PathVariable Integer id) {
        try {
            List<SupportRequestHistory> list = supportService.getHistory(id);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
