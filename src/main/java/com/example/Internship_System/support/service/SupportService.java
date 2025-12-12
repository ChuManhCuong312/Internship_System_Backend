package com.example.Internship_System.support.service;

import com.example.Internship_System.support.dto.SupportDTO;
import com.example.Internship_System.support.dto.SupportRequestDTO;
import com.example.Internship_System.support.dto.TablePaging;
import com.example.Internship_System.support.entity.SupportRequest;
import com.example.Internship_System.support.entity.SupportRequestHistory;
import com.example.Internship_System.support.entity.SupportStatus;
import com.example.Internship_System.support.entity.SupportType;
import com.example.Internship_System.repository.SupportRequestRepository;
import com.example.Internship_System.notification.entity.Notification;
import com.example.Internship_System.notification.repository.NotificationRepository;
import com.example.Internship_System.repository.SupportRequestHistoryRepository;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupportService {

    private final SupportRequestRepository supportRequestRepository;
    private final SupportRequestHistoryRepository historyRepository;
    private final NotificationRepository notificationRepository;

    public SupportService(SupportRequestRepository supportRequestRepository,
            SupportRequestHistoryRepository historyRepository, NotificationRepository notificationRepository) {
        this.supportRequestRepository = supportRequestRepository;
        this.historyRepository = historyRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<SupportRequest> getAll() {
        return supportRequestRepository.findAllByOrderByProcessedDateDesc();
    }

    public SupportRequest getById(Integer id) {
        return supportRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu hỗ trợ"));
    }

    public List<SupportRequest> getMyRequests(Integer internId) {
        return supportRequestRepository.findByInternIdOrderByProcessedDateDesc(internId);
    }

    public TablePaging<SupportRequestDTO> filter(SupportStatus status, SupportType type, Integer internId,
            String keyword,
            Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        var result = supportRequestRepository.findByCriteria(status, type, internId, keyword, pageable);
        return new TablePaging<>(result);
    }

    @Transactional
    public SupportRequest createSupportRequest(SupportDTO dto, Integer internId) {
        SupportType type = SupportType.valueOf(dto.getSupportType().toUpperCase());
        SupportRequest req = SupportRequest.builder()
                .internId(internId)
                .supportType(type)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(SupportStatus.OPEN)
                .build();

        SupportRequest saved = supportRequestRepository.save(req);

        SupportRequestHistory h = SupportRequestHistory.builder()
                .request(saved)
                .oldStatus(null)
                .newStatus(SupportStatus.OPEN)
                .changeDate(LocalDateTime.now())
                .changedBy(internId)
                .remarks("Tạo yêu cầu")
                .build();
        historyRepository.save(h);

        return saved;
    }

    @Transactional
    public SupportRequest approve(Integer id, Integer hrId, String response) {
        SupportRequest req = getById(id);
        if (req.getStatus() != SupportStatus.OPEN) {
            throw new RuntimeException("Chỉ xử lý yêu cầu ở trạng thái Chờ xử lý");
        }
        SupportStatus old = req.getStatus();
        req.setStatus(SupportStatus.RESOLVED);
        req.setProcessedBy(hrId);
        req.setProcessedDate(LocalDateTime.now());
        if (response != null && !response.trim().isEmpty()) {
            req.setResponse(response);
        }
        SupportRequest saved = supportRequestRepository.save(req);

        SupportRequestHistory h = SupportRequestHistory.builder()
                .request(saved)
                .oldStatus(old)
                .newStatus(SupportStatus.RESOLVED)
                .changeDate(LocalDateTime.now())
                .changedBy(hrId)
                .remarks(response)
                .build();
        historyRepository.save(h);

        return saved;
    }

    @Transactional
    public SupportRequest reject(Integer id, Integer hrId, String response) {
        SupportRequest req = getById(id);
        if (req.getStatus() != SupportStatus.IN_PROGRESS) {
            throw new RuntimeException("Chỉ xử lý yêu cầu ở trạng thái Chờ xử lý");
        }
        if (response == null || response.trim().isEmpty()) {
            throw new RuntimeException("Vui lòng nhập phản hồi khi từ chối");
        }
        SupportStatus old = req.getStatus();
        req.setStatus(SupportStatus.REJECTED);
        req.setProcessedBy(hrId);
        req.setProcessedDate(LocalDateTime.now());
        req.setRejectionReason(response);
        req.setResponse(response);
        SupportRequest saved = supportRequestRepository.save(req);

        SupportRequestHistory h = SupportRequestHistory.builder()
                .request(saved)
                .oldStatus(old)
                .newStatus(SupportStatus.REJECTED)
                .changeDate(LocalDateTime.now())
                .changedBy(hrId)
                .remarks(response)
                .build();
        historyRepository.save(h);

        return saved;
    }

    @Transactional
    public SupportRequest updateStatus(Integer id, Integer hrId, SupportStatus status) {
        SupportRequest req = getById(id);
        var oldStatus = req.getStatus();
        req.setStatus(status);
        req.setProcessedBy(hrId);
        req.setProcessedDate(LocalDateTime.now());
        SupportRequest saved = supportRequestRepository.save(req);

        SupportRequestHistory h = SupportRequestHistory.builder()
                .request(saved)
                .oldStatus(oldStatus)
                .newStatus(status)
                .changeDate(LocalDateTime.now())
                .changedBy(req.getProcessedBy())
                .remarks("Cập nhật trạng thái")
                .build();
        historyRepository.save(h);

        Notification noti = Notification.builder()
                .title("Yêu cầu hỗ trợ đã được cập nhật")
                .message("Cán bộ QLNS đã cập nhật trạng thái yêu cầu hỗ trợ thành " + getStatusString(status))
                .type("SUPPORT")
                .internId(req.getInternId())
                .build();
        notificationRepository.save(noti);
        return saved;
    }

    public List<SupportRequestHistory> getHistory(Integer supportId) {
        return historyRepository.findByRequest_SupportIdOrderByChangeDateAsc(supportId);
    }

    private String getStatusString(SupportStatus status) {
        switch (status) {
            case OPEN:
                return "Chờ xử lý";
            case IN_PROGRESS:
                return "Đang xử lý";
            case RESOLVED:
                return "Đã giải quyết";
            case REJECTED:
                return "Đã từ chối";
            default:
                return "Trạng thái không hợp lệ";
        }
    }
}
