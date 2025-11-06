package com.example.Internship_System.intern.controller;

import com.example.Internship_System.intern.entity.Attendance;
import com.example.Internship_System.intern.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/attendances")
@CrossOrigin(origins = "*")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    //CREATE - Add new attendance
    @PostMapping
    public ResponseEntity<Attendance> createAttendance(@RequestBody Attendance attendance) {
        try {
            Attendance saveAttendance = attendanceService.save(attendance);
            return new ResponseEntity<>(saveAttendance, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ all attendances
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        try {
            List<Attendance> attendances = attendanceService.findAll();
            if (attendances.isEmpty()) {
                return new ResponseEntity<>(attendances, HttpStatus.OK);

            }
            return new ResponseEntity<>(attendances, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ attendance by id
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable("id") int id) {
        Optional<Attendance> attendance = attendanceService.findById(id);
        return attendance.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //READ attendance by intern id
    @GetMapping("/intern/{internId}")
    public ResponseEntity<Attendance> getAttendanceByInternId(@PathVariable("internId") int internId) {
        Optional<Attendance> attendance = attendanceService.findByInternId(internId);
        return attendance.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //Update attendance
    @PutMapping("/id")
    public ResponseEntity<Attendance> updateAttendance9(@PathVariable("id") int id, @RequestBody Attendance attendance) {
        Optional<Attendance> existingAttendance = attendanceService.findById(id);
        if (existingAttendance.isPresent()) {
            Attendance attendanceToUpdate = existingAttendance.get();
            attendanceToUpdate.setInternId(attendance.getInternId());
            attendanceToUpdate.setDate(attendance.getDate());
            attendanceToUpdate.setCheckIn(attendance.getCheckIn());
            attendanceToUpdate.setCheckOut(attendance.getCheckOut());
            attendanceToUpdate.setLocation(attendance.getLocation());

            return new ResponseEntity<>(attendanceService.save(attendanceToUpdate),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/id")
    public ResponseEntity<Attendance> partialUpdateAttendance(@PathVariable("id") int id,
                                                              @RequestBody Attendance attendance) {
        Optional<Attendance> existingAttendance = attendanceService.findById(id);

        if (existingAttendance.isPresent()) {
            Attendance attendanceToUpdate = existingAttendance.get();

            if (Attendance.getInternId() != null) {
                attendanceToUpdate.setInternId(attendance.getInternId());
            }
            if (Attendance.getDate() != null) {
                attendanceToUpdate.setDate(attendance.getDate());
            }
            if (Attendance.getCheckIn() != null) {
                attendanceToUpdate.setCheckIn(attendance.getCheckIn());
            }
            if (Attendance.getCheckOut() != null) {
                attendanceToUpdate.setCheckOut(attendance.getCheckOut());
            }
            if (Attendance.getLocation() != null) {
                attendanceToUpdate.setLocation(attendance.getLocation());
            }

            return new ResponseEntity<>(attendanceService.save(attendanceToUpdate), HttpStatus.OK);

    } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}
//DELETE by id
    @DeleteMapping("/id")
    public ResponseEntity<HttpStatus> deleteAttendance(@PathVariable("id") int id){
     try {
         Optional<Attendance> attendance = attendanceService.findById(id);
         if (attendance.isPresent()){
             attendanceService.deleteById(id);
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
         } else {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
     }catch (Exception e){
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
    }
}



