package com.example.Internship_System.intern.service;


import com.example.Internship_System.intern.dto.LeaveRequestDTO;
import com.example.Internship_System.intern.entity.LeaveRequest;
import com.example.Internship_System.intern.entity.LeaveStatus;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.program.entity.ProgramStatus;
import com.example.Internship_System.repository.ProgramRepository;
import com.example.Internship_System.repository.LeaveRequestRepository;
import com.example.Internship_System.notification.service.NotificationService;
import com.example.Internship_System.repository.TeamInternRepository;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.team.entity.TeamIntern;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.auth.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final ProgramRepository programRepository;
    private final TeamInternRepository teamInternRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               ProgramRepository programRepository,
                               TeamInternRepository teamInternRepository,
                               UserRepository userRepository,
                               NotificationService notificationService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.programRepository = programRepository;
        this.teamInternRepository = teamInternRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }


    // Tạo đơn nghỉ phép
    @Transactional
    public LeaveRequest createLeaveRequest(LeaveRequestDTO dto, Integer internId) {
        Program program = programRepository.findProgramByInternId(internId)
                .orElseThrow(() -> new RuntimeException("Bạn chưa được phân vào chương trình thực tập nào"));


        if (program.getProgramStatus() != ProgramStatus.ON_GOING) {
            throw new RuntimeException("Chương trình thực tập của bạn chưa bắt đầu hoặc đã kết thúc");
        }

        validateDates(dto.getStartDate(), dto.getEndDate());
        checkOverlappingRequests(internId, dto.getStartDate(), dto.getEndDate());


        LeaveRequest leaveRequest = LeaveRequest.builder()
                .internId(internId)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .status(LeaveStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .build();


        return leaveRequestRepository.save(leaveRequest);
    }


    // HR duyệt đơn
    @Transactional
    public LeaveRequest approveLeave(Integer leaveId, Integer hrId) {
        LeaveRequest request = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nghỉ phép"));


        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể duyệt các đơn đang ở trạng thái 'Chờ duyệt'");
        }


        request.setStatus(LeaveStatus.APPROVED);
        request.setProcessedBy(hrId);
        LeaveRequest saved = leaveRequestRepository.save(request);


        notificationService.createLeaveApprovedNotification(
                saved.getInternId(),
                saved.getStartDate(),
                saved.getEndDate()
        );


        return saved;
    }


    // HR từ chối đơn
    @Transactional
    public LeaveRequest rejectLeave(Integer leaveId, Integer hrId, String reason) {
        LeaveRequest request = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nghỉ phép"));


        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể từ chối các đơn đang ở trạng thái 'Chờ duyệt'");
        }


        if (reason == null || reason.trim().isEmpty()) {
            throw new RuntimeException("Vui lòng nhập lý do từ chối");
        }


        request.setStatus(LeaveStatus.REJECTED);
        request.setProcessedBy(hrId);
        request.setRejectionReason(reason);
        LeaveRequest saved = leaveRequestRepository.save(request);


        notificationService.createLeaveRejectedNotification(
                saved.getInternId(),
                saved.getStartDate(),
                saved.getEndDate(),
                saved.getRejectionReason()
        );


        return saved;
    }


    // Lấy danh sách đơn nghỉ phép của intern
    public List<LeaveRequest> getLeaveRequestsByIntern(Integer internId) {
        return leaveRequestRepository.findByInternIdOrderByRequestDateDesc(internId);
    }


    // Lấy đơn nghỉ phép của intern đang đăng nhập
    public List<LeaveRequest> getMyLeaveRequests(Integer internId) {
        return leaveRequestRepository.findByInternIdOrderByRequestDateDesc(internId);
    }


    // Lấy chi tiết đơn nghỉ phép
    public LeaveRequest getLeaveRequestById(Integer leaveId, Integer internId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nghỉ phép"));


        if (!leaveRequest.getInternId().equals(internId)) {
            throw new RuntimeException("Bạn không có quyền truy cập đơn nghỉ phép này");
        }


        return leaveRequest;
    }


    // Hủy đơn nghỉ phép (chỉ khi PENDING)
    @Transactional
    public void cancelLeaveRequest(Integer leaveId, Integer internId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nghỉ phép"));


        if (!leaveRequest.getInternId().equals(internId)) {
            throw new RuntimeException("Bạn không có quyền hủy đơn nghỉ phép này");
        }


        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy các đơn đang ở trạng thái 'Chờ duyệt'");
        }


        leaveRequestRepository.delete(leaveRequest);
    }


    // Lấy tất cả đơn theo status (cho HR)
    public List<LeaveRequest> getLeaveRequestsByStatus(LeaveStatus status) {
        return leaveRequestRepository.findByStatusOrderByRequestDateAsc(status);
    }


    public List<Map<String, Object>> getAllLeaveRequestsForHR(LeaveStatus status) {
        List<TeamIntern> teamInterns = teamInternRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();


        if (teamInterns.isEmpty()) {
            return result;
        }

        List<LeaveRequest> leaves;

        if (status != null) {
            leaves = leaveRequestRepository.findByStatus(status);
        } else {
            leaves = leaveRequestRepository.findAll();
        }

        Map<Integer, List<LeaveRequest>> leavesByIntern = leaves.stream()
                .collect(Collectors.groupingBy(LeaveRequest::getInternId));


        for (TeamIntern teamIntern : teamInterns) {
            if (teamIntern.getTeam() == null || teamIntern.getTeam().getProgram() == null) {
                continue;
            }


            Program program = teamIntern.getTeam().getProgram();


            InternProfile intern = teamIntern.getIntern();
            if (intern == null) {
                continue;
            }


            int internId = intern.getInternId();
            List<LeaveRequest> internLeaves = leavesByIntern.get(internId);
            if (internLeaves == null || internLeaves.isEmpty()) {
                continue;
            }


            User user = null;
            if (intern.getUserId() != null) {
                user = userRepository.findById(intern.getUserId()).orElse(null);
            }


            for (LeaveRequest leave : internLeaves) {
                Map<String, Object> item = new HashMap<>();
                item.put("leaveId", leave.getLeaveId());
                item.put("internId", internId);
                item.put("fullName", user != null ? user.getFullName() : null);
                item.put("programId", program.getProgramId());
                item.put("programName", program.getName());
                item.put("startDate", leave.getStartDate());
                item.put("endDate", leave.getEndDate());
                item.put("status", leave.getStatus());
                item.put("reason", leave.getReason());
                item.put("requestDate", leave.getRequestDate());


                Integer processedBy = leave.getProcessedBy();
                String hrName = null;
                if (processedBy != null) {
                    User hrUser = userRepository.findById(processedBy).orElse(null);
                    if (hrUser != null) {
                        hrName = hrUser.getFullName();
                    }
                }


                item.put("processedBy", processedBy);
                item.put("hrName", hrName);
                item.put("rejectionReason", leave.getRejectionReason());


                result.add(item);
            }
        }


        return result;
    }


    public List<Map<String, Object>> getDailyLeaveForHR(LocalDate date, LeaveStatus status) {
        List<TeamIntern> teamInterns = teamInternRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();


        if (teamInterns.isEmpty()) {
            return result;
        }

        List<LeaveRequest> leaves;

        if (status != null) {
            leaves = leaveRequestRepository.findByStatus(status);
        } else {
            leaves = leaveRequestRepository.findAll();
        }

        leaves = leaves.stream()
                .filter(lr -> lr.getRequestDate() != null)
                .filter(lr -> lr.getRequestDate().toLocalDate().equals(date))
                .collect(Collectors.toList());

        Map<Integer, List<LeaveRequest>> leavesByIntern = new HashMap<>();
        for (LeaveRequest leave : leaves) {
            leavesByIntern
                    .computeIfAbsent(leave.getInternId(), k -> new ArrayList<>())
                    .add(leave);
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
            List<LeaveRequest> internLeaves = leavesByIntern.get(internId);
            if (internLeaves == null || internLeaves.isEmpty()) {
                continue;
            }


            User user = null;
            if (intern.getUserId() != null) {
                user = userRepository.findById(intern.getUserId()).orElse(null);
            }


            for (LeaveRequest leave : internLeaves) {
                Map<String, Object> item = new HashMap<>();
                item.put("leaveId", leave.getLeaveId());
                item.put("internId", internId);
                item.put("fullName", user != null ? user.getFullName() : null);
                item.put("programId", program.getProgramId());
                item.put("programName", program.getName());
                item.put("startDate", leave.getStartDate());
                item.put("endDate", leave.getEndDate());
                item.put("status", leave.getStatus());
                item.put("reason", leave.getReason());
                item.put("requestDate", leave.getRequestDate());


                Integer processedBy = leave.getProcessedBy();
                String hrName = null;
                if (processedBy != null) {
                    User hrUser = userRepository.findById(processedBy).orElse(null);
                    if (hrUser != null) {
                        hrName = hrUser.getFullName();
                    }
                }


                item.put("processedBy", processedBy);
                item.put("hrName", hrName);
                item.put("rejectionReason", leave.getRejectionReason());


                result.add(item);
            }
        }


        return result;
    }


    public List<Map<String, Object>> getMonthlyLeaveForHR(int year, int month, LeaveStatus status) {
        List<TeamIntern> teamInterns = teamInternRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();


        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);


        if (teamInterns.isEmpty()) {
            return result;
        }

        List<LeaveRequest> leaves;

        if (status != null) {
            leaves = leaveRequestRepository.findByStatus(status);
        } else {
            leaves = leaveRequestRepository.findAll();
        }

        LocalDate finalMonthStart = monthStart;
        LocalDate finalMonthEnd = monthEnd;

        leaves = leaves.stream()
                .filter(lr -> lr.getRequestDate() != null)
                .filter(lr -> {
                    LocalDate rDate = lr.getRequestDate().toLocalDate();
                    return !rDate.isBefore(finalMonthStart) && !rDate.isAfter(finalMonthEnd);
                })
                .collect(Collectors.toList());

        Map<Integer, List<LeaveRequest>> leavesByIntern = leaves.stream()
                .collect(Collectors.groupingBy(LeaveRequest::getInternId));


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


            if (programEnd.isBefore(monthStart) || programStart.isAfter(monthEnd)) {
                continue;
            }


            InternProfile intern = teamIntern.getIntern();
            if (intern == null) {
                continue;
            }


            int internId = intern.getInternId();
            List<LeaveRequest> internLeaves = leavesByIntern.get(internId);
            if (internLeaves == null || internLeaves.isEmpty()) {
                continue;
            }


            User user = null;
            if (intern.getUserId() != null) {
                user = userRepository.findById(intern.getUserId()).orElse(null);
            }


            for (LeaveRequest leave : internLeaves) {
                Map<String, Object> item = new HashMap<>();
                item.put("leaveId", leave.getLeaveId());
                item.put("internId", internId);
                item.put("fullName", user != null ? user.getFullName() : null);
                item.put("programId", program.getProgramId());
                item.put("programName", program.getName());
                item.put("startDate", leave.getStartDate());
                item.put("endDate", leave.getEndDate());
                item.put("status", leave.getStatus());
                item.put("reason", leave.getReason());
                item.put("requestDate", leave.getRequestDate());


                Integer processedBy = leave.getProcessedBy();
                String hrName = null;
                if (processedBy != null) {
                    User hrUser = userRepository.findById(processedBy).orElse(null);
                    if (hrUser != null) {
                        hrName = hrUser.getFullName();
                    }
                }


                item.put("processedBy", processedBy);
                item.put("hrName", hrName);
                item.put("rejectionReason", leave.getRejectionReason());


                result.add(item);
            }
        }


        return result;
    }


    // Validate ngày
    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Ngày bắt đầu không được sau ngày kết thúc");
        }


        if (startDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Ngày bắt đầu không được là ngày trong quá khứ");
        }


        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (daysBetween > 30) {
            throw new RuntimeException("Không thể xin nghỉ quá 30 ngày liên tục");
        }
    }


    // Kiểm tra trùng lặp
    private void checkOverlappingRequests(Integer internId, LocalDate startDate, LocalDate endDate) {
        List<LeaveRequest> approvedOverlapping = leaveRequestRepository
                .findOverlappingRequests(internId, startDate, endDate, LeaveStatus.APPROVED);


        if (!approvedOverlapping.isEmpty()) {
            throw new RuntimeException("Đã có đơn nghỉ phép được duyệt trong khoảng thời gian này");
        }


        List<LeaveRequest> pendingOverlapping = leaveRequestRepository
                .findOverlappingRequests(internId, startDate, endDate, LeaveStatus.PENDING);


        if (!pendingOverlapping.isEmpty()) {
            throw new RuntimeException("Đã có đơn nghỉ phép đang chờ duyệt trong khoảng thời gian này");
        }
    }
}

