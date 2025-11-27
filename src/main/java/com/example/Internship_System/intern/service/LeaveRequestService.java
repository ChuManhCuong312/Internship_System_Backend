package com.example.Internship_System.intern.service;


import com.example.Internship_System.intern.dto.LeaveRequestDTO;
import com.example.Internship_System.intern.entity.LeaveRequest;
import com.example.Internship_System.intern.entity.LeaveStatus;
import com.example.Internship_System.repository.LeaveRequestRepository;
import com.example.Internship_System.notification.entity.Notification;
import com.example.Internship_System.notification.service.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class LeaveRequestService {


    private final LeaveRequestRepository leaveRequestRepository;


    private final NotificationService notificationService;


    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, NotificationService notificationService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.notificationService = notificationService;
    }


    // Tạo đơn nghỉ phép
    @Transactional
    public LeaveRequest createLeaveRequest(LeaveRequestDTO dto, Integer internId) {
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


        String title = "Đơn nghỉ phép đã được duyệt";
        String message = "Đơn nghỉ phép từ " + saved.getStartDate() + " đến " + saved.getEndDate()
                + " đã được HR duyệt.";
        Notification notification = new Notification(saved.getInternId(), title, message, "LEAVE_REQUEST");
        notificationService.save(notification);


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


        String title = "Đơn nghỉ phép đã bị từ chối";
        String message = "Đơn nghỉ phép từ " + saved.getStartDate() + " đến " + saved.getEndDate()
                + " đã bị HR từ chối. Lý do: " + saved.getRejectionReason();
        Notification notification = new Notification(saved.getInternId(), title, message, "LEAVE_REQUEST");
        notificationService.save(notification);


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

