package com.example.Internship_System.support.controller;

import com.example.Internship_System.support.dto.CreateSupportRequestDTO;
import com.example.Internship_System.support.entity.SupportRequest;
import com.example.Internship_System.support.entity.SupportStatus;
import com.example.Internship_System.support.entity.SupportStatusHistory;
import com.example.Internship_System.support.entity.SupportType;
import com.example.Internship_System.support.service.SupportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support-requests")
@CrossOrigin(origins = "*")
public class SupportController {

    @Autowired
    private SupportService supportService;

    // Intern tạo support request mới
    @PostMapping
    @PreAuthorize("hasRole('INTERN')")
    public ResponseEntity<?> createSupportRequest(
            @Valid @RequestBody CreateSupportRequestDTO request,
            @RequestParam Integer internId) {
        try {
            SupportRequest created = supportService.createSupportRequest(request, internId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Tạo yêu cầu hỗ trợ thành công");
            response.put("data", created);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // HR lấy tất cả support requests
    @GetMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> getAllSupportRequests() {
        try {
            List<SupportRequest> requests = supportService.getAllSupportRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // HR lấy chi tiết support request
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> getSupportRequestById(@PathVariable Integer id) {
        try {
            SupportRequest request = supportService.getSupportRequestById(id);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // HR lọc support requests
    @GetMapping("/filter")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> filterSupportRequests(
            @RequestParam(required = false) SupportStatus status,
            @RequestParam(required = false) SupportType type,
            @RequestParam(required = false) Integer internId) {
        try {
            List<SupportRequest> requests = supportService.getSupportRequestsByFilters(status, type, internId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // HR duyệt support request
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> approveSupportRequest(
            @PathVariable Integer id,
            @RequestParam Integer hrId) {
        try {
            SupportRequest approved = supportService.approveSupportRequest(id, hrId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã duyệt yêu cầu hỗ trợ");
            response.put("data", approved);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // HR từ chối support request
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> rejectSupportRequest(
            @PathVariable Integer id,
            @RequestParam Integer hrId,
            @RequestParam String rejectionReason) {
        try {
            SupportRequest rejected = supportService.rejectSupportRequest(id, hrId, rejectionReason);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã từ chối yêu cầu hỗ trợ");
            response.put("data", rejected);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // HR xem lịch sử thay đổi status
    @GetMapping("/{id}/history")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> getSupportRequestHistory(@PathVariable Integer id) {
        try {
            List<SupportStatusHistory> history = supportService.getSupportStatusHistory(id);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Intern xem support requests của mình
    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('INTERN')")
    public ResponseEntity<?> getMySupportRequests(@RequestParam Integer internId) {
        try {
            List<SupportRequest> requests = supportService.getSupportRequestsByFilters(null, null, internId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
