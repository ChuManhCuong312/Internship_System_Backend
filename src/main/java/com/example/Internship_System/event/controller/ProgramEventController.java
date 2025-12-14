package com.example.Internship_System.event.controller;

import com.example.Internship_System.event.dto.ProgramEventRequest;
import com.example.Internship_System.event.entity.ProgramEvent;
import com.example.Internship_System.event.service.ProgramEventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/program-events")
public class ProgramEventController {

    private final ProgramEventService eventService;

    // Constructor injection
    public ProgramEventController(ProgramEventService eventService) {
        this.eventService = eventService;
    }

    // ➕ Thêm sự kiện
    @PostMapping
    public ResponseEntity<String> create(
            @RequestBody @Valid ProgramEventRequest request) {

        eventService.create(request);
        return ResponseEntity.ok("Tạo sự kiện thành công");
    }

    // ✏️ Sửa sự kiện
    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable Integer id,
            @RequestBody @Valid ProgramEventRequest request) {

        eventService.update(id, request);
        return ResponseEntity.ok("Cập nhật sự kiện thành công");
    }

    // 🗑️ Xóa sự kiện
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {

        eventService.delete(id);
        return ResponseEntity.ok("Xóa sự kiện thành công");
    }
    @GetMapping("/program/{programId}")
    public ResponseEntity<List<ProgramEvent>> getByProgram(
            @PathVariable Integer programId) {

        return ResponseEntity.ok(eventService.getByProgramId(programId));
    }
}
