package com.example.Internship_System.intern.service;

import com.example.Internship_System.intern.entity.Attendance;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.program.entity.ProgramStatus;
import com.example.Internship_System.repository.AttendanceRepository;
import com.example.Internship_System.repository.InternRepository;
import com.example.Internship_System.repository.ProgramRepository;
import com.example.Internship_System.repository.TeamInternRepository;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.team.entity.TeamIntern;
import com.example.Internship_System.auth.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private TeamInternRepository teamInternRepository;

    @Autowired
    private UserRepository userRepository;

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

        Program program = programRepository.findProgramByInternId(internId)
                .orElseThrow(() -> new RuntimeException("Bạn chưa được phân vào chương trình thực tập nào"));

        if (program.getProgramStatus() != ProgramStatus.ON_GOING) {
            throw new RuntimeException("Chương trình thực tập của bạn chưa bắt đầu hoặc đã kết thúc");
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
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

        List<Attendance> monthlyAttendances = repository.findAttendanceByInternIdAndDateRange(
                internId, monthStart, monthEnd);

        Map<LocalDate, Attendance> attendanceByDate = new HashMap<>();
        for (Attendance attendance : monthlyAttendances) {
            if (attendance.getDate() != null) {
                attendanceByDate.put(attendance.getDate(), attendance);
            }
        }

        Optional<Program> programOpt = programRepository.findProgramByInternId(internId);

        long workingDays = 0;
        long absentDays = 0;

        if (programOpt.isPresent()) {
            Program program = programOpt.get();

            LocalDate programStart = program.getStartDate() != null
                    ? program.getStartDate().toLocalDate()
                    : monthStart;
            LocalDate programEnd = program.getEndDate() != null
                    ? program.getEndDate().toLocalDate()
                    : monthEnd;

            LocalDate workingStart = programStart.isAfter(monthStart) ? programStart : monthStart;
            LocalDate workingEnd = programEnd.isBefore(monthEnd) ? programEnd : monthEnd;

            if (!workingEnd.isBefore(workingStart)) {
                LocalDate current = workingStart;
                while (!current.isAfter(workingEnd)) {
                    DayOfWeek dayOfWeek = current.getDayOfWeek();
                    if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                        workingDays++;
                        if (!attendanceByDate.containsKey(current)) {
                            absentDays++;
                        }
                    }
                    current = current.plusDays(1);
                }
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("month", month);
        stats.put("year", year);
        stats.put("totalDays", monthlyAttendances.size());
        stats.put("workingDays", workingDays);
        stats.put("absentDays", absentDays);

        long lateDays = monthlyAttendances.stream()
                .filter(a -> a.getCheckIn() != null &&
                        a.getCheckIn().isAfter(LocalTime.of(8, 30)))
                .count();

        long insufficientDays = monthlyAttendances.stream()
                .filter(a -> a.getWorkingMinutes() == null || a.getWorkingMinutes() < Attendance.REQUIRED_WORKING_MINUTES)
                .count();

        stats.put("insufficientDays", insufficientDays);
        stats.put("sufficientDays", monthlyAttendances.size() - insufficientDays);
        stats.put("lateDays", lateDays);
        stats.put("onTimeDays", monthlyAttendances.size() - lateDays);
        stats.put("attendances", monthlyAttendances);

        return stats;
    }

    public List<Map<String, Object>> getDailyAttendanceForHR(LocalDate date) {
        List<TeamIntern> teamInterns = teamInternRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        if (teamInterns.isEmpty()) {
            return result;
        }

        List<Attendance> attendances = repository.findAllByDate(date);
        Map<Integer, Attendance> attendanceByIntern = new HashMap<>();
        for (Attendance attendance : attendances) {
            attendanceByIntern.put(attendance.getInternId(), attendance);
        }

        for (TeamIntern teamIntern : teamInterns) {
            if (teamIntern.getTeam() == null || teamIntern.getTeam().getProgram() == null) {
                continue;
            }

            Program program = teamIntern.getTeam().getProgram();

            LocalDate programStart = program.getStartDate() != null
                    ? program.getStartDate().toLocalDate()
                    : LocalDate.MIN;
            LocalDate programEnd = program.getEndDate() != null
                    ? program.getEndDate().toLocalDate()
                    : LocalDate.MAX;

            if (date.isBefore(programStart) || date.isAfter(programEnd)) {
                continue;
            }

            InternProfile intern = teamIntern.getIntern();
            if (intern == null) {
                continue;
            }

            int internId = intern.getInternId();

            User user = null;
            if (intern.getUserId() != null) {
                user = userRepository.findById(intern.getUserId())
                        .orElse(null);
            }

            Attendance attendance = attendanceByIntern.get(internId);

            Map<String, Object> item = new HashMap<>();
            item.put("internId", internId);
            item.put("fullName", user != null ? user.getFullName() : null);
            item.put("programId", program.getProgramId());
            item.put("programName", program.getName());
            item.put("date", date);

            if (attendance != null) {
                item.put("checkIn", attendance.getCheckIn());
                item.put("checkOut", attendance.getCheckOut());
                item.put("status", attendance.getStatus());
            } else {
                item.put("checkIn", null);
                item.put("checkOut", null);
                item.put("status", "ABSENT");
            }

            result.add(item);
        }

        return result;
    }

    public List<Map<String, Object>> getMonthlyStatisticsForHR(int year, int month) {
        List<TeamIntern> teamInterns = teamInternRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

        if (teamInterns.isEmpty()) {
            return result;
        }

        for (TeamIntern teamIntern : teamInterns) {
            if (teamIntern.getTeam() == null || teamIntern.getTeam().getProgram() == null) {
                continue;
            }

            Program program = teamIntern.getTeam().getProgram();

            LocalDate programStart = program.getStartDate() != null
                    ? program.getStartDate().toLocalDate()
                    : LocalDate.MIN;
            LocalDate programEnd = program.getEndDate() != null
                    ? program.getEndDate().toLocalDate()
                    : LocalDate.MAX;

            // Bỏ qua nếu khoảng thời gian program không giao với tháng filter
            if (programEnd.isBefore(monthStart) || programStart.isAfter(monthEnd)) {
                continue;
            }

            InternProfile intern = teamIntern.getIntern();
            if (intern == null) {
                continue;
            }

            int internId = intern.getInternId();

            User user = null;
            if (intern.getUserId() != null) {
                user = userRepository.findById(intern.getUserId())
                        .orElse(null);
            }

            Map<String, Object> stats = getMonthlyStatistics(internId, year, month);

            Map<String, Object> item = new HashMap<>();
            item.put("internId", internId);
            item.put("fullName", user != null ? user.getFullName() : null);
            item.put("programId", program.getProgramId());
            item.put("programName", program.getName());
            item.putAll(stats);

            result.add(item);
        }

        return result;
    }
}
