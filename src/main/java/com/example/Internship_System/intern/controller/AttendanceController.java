package com.example.Internship_System.intern.controller;

import com.example.Internship_System.intern.entity.Attendance;
import com.example.Internship_System.intern.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendances")
@CrossOrigin(origins = "*")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    //CREATE - Add new attendance
    @PostMapping
    public ResponseEntity<?> createAttendance(@RequestBody Attendance attendance) {
        try {
            Attendance savedAttendance = attendanceService.save(attendance);
            return new ResponseEntity<>(savedAttendance, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể tạo chấm công"));
        }
    }

    //READ all attendances
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        try {
            List<Attendance> attendances = attendanceService.findAll();
            return new ResponseEntity<>(attendances, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ attendance by id
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable("id") int id) {
        Optional<Attendance> attendance = attendanceService.findById(id);
        return attendance.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //READ attendance by intern id
    @GetMapping("/intern/{internId}")
    public ResponseEntity<?> getAttendanceByInternId(@PathVariable int internId) {
        try {
            List<Attendance> attendances = attendanceService.findByInternId(internId);
            return new ResponseEntity<>(attendances, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể lấy danh sách"));
        }
    }

    //Update attendance
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAttendance(
            @PathVariable("id") int id,
            @RequestBody Attendance attendance) {
        try {
            Optional<Attendance> existingAttendance = attendanceService.findById(id);

            if (existingAttendance.isPresent()) {
                Attendance attendanceToUpdate = existingAttendance.get();
                attendanceToUpdate.setInternId(attendance.getInternId());
                attendanceToUpdate.setDate(attendance.getDate());
                attendanceToUpdate.setCheckIn(attendance.getCheckIn());
                attendanceToUpdate.setCheckOut(attendance.getCheckOut());

                return new ResponseEntity<>(
                        attendanceService.save(attendanceToUpdate),
                        HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy attendance"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể cập nhật"));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateAttendance(
            @PathVariable("id") int id,
            @RequestBody Attendance attendance) {
        try {
            Optional<Attendance> existingAttendance = attendanceService.findById(id);

            if (existingAttendance.isPresent()) {
                Attendance attendanceToUpdate = existingAttendance.get();

                if (attendance.getDate() != null) {
                    attendanceToUpdate.setDate(attendance.getDate());
                }
                if (attendance.getCheckIn() != null) {
                    attendanceToUpdate.setCheckIn(attendance.getCheckIn());
                }
                if (attendance.getCheckOut() != null) {
                    attendanceToUpdate.setCheckOut(attendance.getCheckOut());
                }

                return new ResponseEntity<>(
                        attendanceService.save(attendanceToUpdate),
                        HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy attendance"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể cập nhật"));
        }
    }

    //DELETE by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttendance(@PathVariable("id") int id) {
        try {
            Optional<Attendance> attendance = attendanceService.findById(id);
            if (attendance.isPresent()) {
                attendanceService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy attendance"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể xóa"));
        }
    }

    @GetMapping("/today/{internId}")
    public ResponseEntity<?> getTodayAttendance(@PathVariable int internId) {
        try {
            Optional<Attendance> today = attendanceService.getTodayAttendance(internId);
            Map<String, Boolean> status = attendanceService.getCheckStatus(internId);

            Map<String, Object> response = new HashMap<>();
            response.put("hasCheckedIn", status.get("hasCheckedIn"));
            response.put("hasCheckedOut", status.get("hasCheckedOut"));
            response.put("attendance", today.orElse(null));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể lấy thông tin hôm nay"));
        }
    }
    @GetMapping("/history/{internId}/range")
    public ResponseEntity<?> getAttendanceByDateRange(
            @PathVariable int internId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Attendance> history = attendanceService.getAttendanceByDateRange(
                    internId, startDate, endDate);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể lấy lịch sử"));
        }
    }
    @GetMapping("/statistics/{internId}")
    public ResponseEntity<?> getAttendanceStatistics(@PathVariable int internId) {
        try {
            Map<String, Object> stats = attendanceService.getAttendanceStatistics(internId);
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể lấy thống kê"));
        }
    }
    @GetMapping("/statistics/{internId}/monthly")
    public ResponseEntity<?> getMonthlyStatistics(
            @PathVariable int internId,
            @RequestParam int year,
            @RequestParam int month) {
        try {
            Map<String, Object> stats = attendanceService.getMonthlyStatistics(
                    internId, year, month);
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể lấy thống kê tháng"));
        }
    }
    @GetMapping("/date")
    public ResponseEntity<?> getAllAttendanceByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Attendance> attendances = attendanceService.getAllAttendanceByDate(date);
            return new ResponseEntity<>(attendances, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể lấy danh sách"));
        }
    }

    @GetMapping("/hr/daily")
    public ResponseEntity<?> getDailyAttendanceForHR(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Map<String, Object>> result = attendanceService.getDailyAttendanceForHR(date);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể lấy danh sách chấm công cho HR"));
        }
    }

    @GetMapping("/hr/monthly")
    public ResponseEntity<?> getMonthlyAttendanceForHR(
            @RequestParam int year,
            @RequestParam int month) {
        try {
            List<Map<String, Object>> result = attendanceService.getMonthlyStatisticsForHR(year, month);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể lấy thống kê chấm công tháng cho HR"));
        }
    }
    @PostMapping("/check-in/{internId}")
    public ResponseEntity<?> checkIn(@PathVariable int internId) {
        try {
            Attendance result = attendanceService.checkIn(internId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Check-in thành công lúc " + result.getCheckIn());
            response.put("attendance", result);
            response.put("status", result.getStatus());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Có lỗi xảy ra khi check-in"));
        }
    }
    @PostMapping("/check-out/{internId}")
    public ResponseEntity<?> checkOut(@PathVariable int internId) {
        try {
            Attendance result = attendanceService.checkOut(internId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Check-out thành công lúc " + result.getCheckOut());
            response.put("attendance", result);
            response.put("status", result.getStatus());
            response.put("workingMinutes", result.getWorkingMinutes());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Có lỗi xảy ra khi check-out"));
        }
    }

}



