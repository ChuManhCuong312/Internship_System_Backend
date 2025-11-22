package com.example.Internship_System.intern.controller;


import com.example.Internship_System.intern.dto.LeaveRequestDTO;
import com.example.Internship_System.intern.entity.LeaveRequest;
import com.example.Internship_System.intern.entity.LeaveStatus;
import com.example.Internship_System.intern.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {


    private final LeaveRequestService leaveRequestService;


    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }


    // Intern tạo đơn nghỉ phép
    @PostMapping
    @PreAuthorize("hasRole('INTERN')")
    public ResponseEntity<?> createLeaveRequest(
            @Valid @RequestBody LeaveRequestDTO request,
            @RequestParam Integer internId) {
        try {
            LeaveRequest created = leaveRequestService.createLeaveRequest(request, internId);


            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Tạo đơn xin nghỉ phép thành công");
            response.put("data", created);


            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // Intern xem danh sách đơn của mình
    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('INTERN')")
    public ResponseEntity<?> getMyLeaveRequests(@RequestParam Integer internId) {
        try {
            List<LeaveRequest> requests = leaveRequestService.getMyLeaveRequests(internId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // Intern xem chi tiết một đơn
    @GetMapping("/{leaveId}")
    @PreAuthorize("hasRole('INTERN')")
    public ResponseEntity<?> getLeaveRequestById(
            @PathVariable Integer leaveId,
            @RequestParam Integer internId) {
        try {
            LeaveRequest request = leaveRequestService.getLeaveRequestById(leaveId, internId);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // Intern hủy đơn
    @DeleteMapping("/{leaveId}")
    @PreAuthorize("hasRole('INTERN')")
    public ResponseEntity<?> cancelLeaveRequest(
            @PathVariable Integer leaveId,
            @RequestParam Integer internId) {
        try {
            leaveRequestService.cancelLeaveRequest(leaveId, internId);


            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Hủy đơn xin nghỉ phép thành công");


            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // HR xem tất cả đơn theo status
    @GetMapping("/all")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> getAllLeaveRequests(@RequestParam(required = false) LeaveStatus status) {
        try {
            List<LeaveRequest> requests;
            if (status != null) {
                requests = leaveRequestService.getLeaveRequestsByStatus(status);
            } else {
                requests = leaveRequestService.getLeaveRequestsByStatus(LeaveStatus.PENDING);
            }
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // HR duyệt đơn
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> approveLeave(
            @PathVariable Integer id,
            @RequestParam Integer hrId) {
        try {
            LeaveRequest approved = leaveRequestService.approveLeave(id, hrId);


            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã duyệt đơn nghỉ phép");
            response.put("data", approved);


            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // HR từ chối đơn
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> rejectLeave(
            @PathVariable Integer id,
            @RequestParam Integer hrId,
            @RequestParam String rejectionReason) {
        try {
            LeaveRequest rejected = leaveRequestService.rejectLeave(id, hrId, rejectionReason);


            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã từ chối đơn nghỉ phép");
            response.put("data", rejected);


            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // HR xem đơn của một intern cụ thể
    @GetMapping("/intern/{internId}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> getLeaveRequestsByIntern(@PathVariable Integer internId) {
        try {
            List<LeaveRequest> requests = leaveRequestService.getLeaveRequestsByIntern(internId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}

