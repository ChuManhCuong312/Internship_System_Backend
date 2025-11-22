package com.example.Internship_System.intern.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private int attendanceId;

    @Column(name = "intern_id", nullable = false)
    private int internId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "check_in")
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;

    public Attendance() {}

    public Attendance(int internId, LocalDate date) {
        this.internId = internId;
        this.date = date;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getInternId() {
        return internId;
    }

    public void setInternId(int internId) {
        this.internId = internId;
    }

    public LocalDate getDate() {return date;}

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }

    public String getStatus() {
        if (checkIn == null && checkOut == null) {
            return "ABSENT";
        }
        if (checkIn == null || checkOut == null) {
            return "INCOMPLETE";
        }

        // Check if late (after 8:30 AM)
        LocalTime lateThreshold = LocalTime.of(8, 30);
        if (checkIn.isAfter(lateThreshold)) {
            return "LATE";
        }

        return "ON_TIME";
    }

    public Long getWorkingMinutes() {
        if (checkIn != null && checkOut != null) {
            return java.time.Duration.between(checkIn, checkOut).toMinutes();
        }
        return null;
    }
}
