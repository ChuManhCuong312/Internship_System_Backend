package com.example.Internship_System.intern.service;

import com.example.Internship_System.intern.entity.Attendance;
import com.example.Internship_System.repository.AttendanceRepository;
import com.example.Internship_System.repository.InternRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository repository;

    @Autowired
    private InternRepository internRepository;

    public Attendance save(Attendance attendance) {
        return repository.save(attendance);
    }

    public List<Attendance> findAll() {
        return repository.findAll();
    }

    public Optional<Attendance> findById(int id) {
        return repository.findById(id);
    }

    public List<Attendance> findByInternId(int internId) {
        return repository.findByInternId(internId);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Transactional
    public Attendance checkIn(int internId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (!internRepository.existsById(internId)) {
            throw new RuntimeException("Intern không tồn tại");
        }
        Optional<Attendance> existingAttendance = repository.findByInternIdAndDate(internId, today);

        if (existingAttendance.isPresent() && existingAttendance.get().getCheckIn() != null) {
            throw new RuntimeException("Bạn đã check-in hôm nay rồi lúc " +
                    existingAttendance.get().getCheckIn().toString());
        }

        Attendance attendance;
        if (existingAttendance.isPresent()) {
            attendance = existingAttendance.get();
            attendance.setCheckIn(now);
        } else {
            attendance = new Attendance(internId, today);
            attendance.setCheckIn(now);
        }

        return repository.save(attendance);
    }
    @Transactional
    public Attendance checkOut(int internId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        Attendance attendance = repository.findByInternIdAndDate(internId, today)
                .orElseThrow(() -> new RuntimeException("Bạn chưa check-in hôm nay"));

        if (attendance.getCheckIn() == null) {
            throw new RuntimeException("Bạn chưa check-in hôm nay");
        }

        if (attendance.getCheckOut() != null) {
            throw new RuntimeException("Bạn đã check-out rồi lúc " +
                    attendance.getCheckOut().toString());
        }

        attendance.setCheckOut(now);
        return repository.save(attendance);
    }
    public Optional<Attendance> getTodayAttendance(int internId) {
        LocalDate today = LocalDate.now();
        return repository.findByInternIdAndDate(internId, today);
    }
    public List<Attendance> getAttendanceHistory(int internId) {
        return repository.findAttendanceHistoryByInternId(internId);
    }
    public List<Attendance> getAttendanceByDateRange(int internId,
                                                     LocalDate startDate,
                                                     LocalDate endDate) {
        return repository.findAttendanceByInternIdAndDateRange(internId, startDate, endDate);
    }
    public List<Attendance> getAllAttendanceByDate(LocalDate date) {
        return repository.findAllByDate(date);
    }
    public Map<String, Boolean> getCheckStatus(int internId) {
        LocalDate today = LocalDate.now();
        Map<String, Boolean> status = new HashMap<>();
        status.put("hasCheckedIn", repository.hasCheckedInToday(internId, today));
        status.put("hasCheckedOut", repository.hasCheckedOutToday(internId, today));
        return status;
    }
    public Map<String, Object> getAttendanceStatistics(int internId) {
        Map<String, Object> stats = new HashMap<>();

        long totalWorkingDays = repository.countWorkingDaysByInternId(internId);
        long lateDays = repository.countLateDaysByInternId(internId, LocalTime.of(8, 30));

        stats.put("totalWorkingDays", totalWorkingDays);
        stats.put("lateDays", lateDays);
        stats.put("onTimeDays", totalWorkingDays - lateDays);
        stats.put("latePercentage", totalWorkingDays > 0 ?
                (lateDays * 100.0 / totalWorkingDays) : 0);

        return stats;
    }
    public Map<String, Object> getMonthlyStatistics(int internId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<Attendance> monthlyAttendances = repository.findAttendanceByInternIdAndDateRange(
                internId, startDate, endDate);

        Map<String, Object> stats = new HashMap<>();
        stats.put("month", month);
        stats.put("year", year);
        stats.put("totalDays", monthlyAttendances.size());

        long lateDays = monthlyAttendances.stream()
                .filter(a -> a.getCheckIn() != null &&
                        a.getCheckIn().isAfter(LocalTime.of(8, 30)))
                .count();

        stats.put("lateDays", lateDays);
        stats.put("onTimeDays", monthlyAttendances.size() - lateDays);
        stats.put("attendances", monthlyAttendances);

        return stats;
    }


}
