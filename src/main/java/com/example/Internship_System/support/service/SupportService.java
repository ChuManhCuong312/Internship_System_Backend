package com.example.Internship_System.support.service;

import com.example.Internship_System.repository.SupportRequestRepository;
import com.example.Internship_System.repository.SupportStatusHistoryRepository;
import com.example.Internship_System.repository.InternRepository;
import com.example.Internship_System.support.dto.CreateSupportRequestDTO;
import com.example.Internship_System.support.entity.*;
import com.example.Internship_System.intern.entity.InternProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SupportService {

    @Autowired
    private SupportRequestRepository supportRequestRepository;

    @Autowired
    private SupportStatusHistoryRepository supportStatusHistoryRepository;

    @Autowired
    private InternRepository internRepository;

    // Tạo support request mới
    @Transactional
    public SupportRequest createSupportRequest(CreateSupportRequestDTO dto, Integer internId) {
        // Kiểm tra intern tồn tại
        Optional<InternProfile> intern = internRepository.findById(internId);
        if (intern.isEmpty()) {
            throw new RuntimeException("Intern không tồn tại với ID: " + internId);
        }

        SupportRequest supportRequest = SupportRequest.builder()
                .internId(internId)
                .supportType(dto.getSupportType())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(SupportStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .build();

        SupportRequest saved = supportRequestRepository.save(supportRequest);

        // Lưu lịch sử tạo mới
        saveSupportStatusHistory(null, saved, internId, "Tạo yêu cầu hỗ trợ mới");

        return saved;
    }

    // Lấy tất cả support requests
    public List<SupportRequest> getAllSupportRequests() {
        return supportRequestRepository.findAll();
    }

    // Lấy support request theo ID
    public SupportRequest getSupportRequestById(Integer id) {
        return supportRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy support request với ID: " + id));
    }

    // Lọc support requests theo các tiêu chí
    public List<SupportRequest> getSupportRequestsByFilters(
            SupportStatus status,
            SupportType type,
            Integer internId) {

        // Tất cả bộ lọc được cung cấp
        if (status != null && type != null && internId != null) {
            return supportRequestRepository.findByInternIdAndStatusAndSupportType(internId, status, type);
        }

        // Status và Type
        if (status != null && type != null) {
            return supportRequestRepository.findByStatusAndSupportType(status, type);
        }

        // InternId và Status
        if (internId != null && status != null) {
            return supportRequestRepository.findByInternIdAndStatus(internId, status);
        }

        // InternId và Type
        if (internId != null && type != null) {
            return supportRequestRepository.findByInternIdAndSupportType(internId, type);
        }

        // Chỉ Status
        if (status != null) {
            return supportRequestRepository.findByStatus(status);
        }

        // Chỉ Type
        if (type != null) {
            return supportRequestRepository.findBySupportType(type);
        }

        // Chỉ InternId
        if (internId != null) {
            return supportRequestRepository.findByInternId(internId);
        }

        // Không có bộ lọc nào - trả về tất cả
        return supportRequestRepository.findAll();
    }

    // Duyệt support request
    @Transactional
    public SupportRequest approveSupportRequest(Integer id, Integer hrId, String response) {
        SupportRequest supportRequest = getSupportRequestById(id);

        if (supportRequest.getStatus() != SupportStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể duyệt yêu cầu ở trạng thái PENDING");
        }

        SupportRequest oldRequest = cloneSupportRequest(supportRequest);

        supportRequest.setStatus(SupportStatus.APPROVED);
        supportRequest.setProcessedBy(hrId);
        supportRequest.setProcessedDate(LocalDateTime.now());
        supportRequest.setResponse(response);

        SupportRequest updated = supportRequestRepository.save(supportRequest);

        // Lưu lịch sử
        saveSupportStatusHistory(oldRequest, updated, hrId, "Duyệt yêu cầu hỗ trợ. Phản hồi: " + (response != null ? response : ""));

        return updated;
    }

    // Từ chối support request
    @Transactional
    public SupportRequest rejectSupportRequest(Integer id, Integer hrId, String response) {
        if (response == null || response.trim().isEmpty()) {
            throw new RuntimeException("Lý do từ chối không được để trống");
        }

        SupportRequest supportRequest = getSupportRequestById(id);

        if (supportRequest.getStatus() != SupportStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể từ chối yêu cầu ở trạng thái PENDING");
        }

        SupportRequest oldRequest = cloneSupportRequest(supportRequest);

        supportRequest.setStatus(SupportStatus.REJECTED);
        supportRequest.setProcessedBy(hrId);
        supportRequest.setProcessedDate(LocalDateTime.now());
        supportRequest.setResponse(response);

        SupportRequest updated = supportRequestRepository.save(supportRequest);

        // Lưu lịch sử
        saveSupportStatusHistory(oldRequest, updated, hrId, "Từ chối yêu cầu hỗ trợ: " + response);

        return updated;
    }

    // Lấy lịch sử thay đổi status
    public List<SupportStatusHistory> getSupportStatusHistory(Integer supportId) {
        return supportStatusHistoryRepository.findBySupportIdOrderByChangeDateDesc(supportId);
    }

    // Lưu lịch sử thay đổi status
    @Transactional
    public void saveSupportStatusHistory(
            SupportRequest oldRequest,
            SupportRequest newRequest,
            Integer changedBy,
            String remarks) {

        SupportStatusHistory history = SupportStatusHistory.builder()
                .supportId(newRequest.getSupportId())
                .oldStatus(oldRequest != null ? oldRequest.getStatus() : null)
                .newStatus(newRequest.getStatus())
                .changedBy(changedBy)
                .changeDate(LocalDateTime.now())
                .remarks(remarks)
                .build();

        supportStatusHistoryRepository.save(history);
    }

    // Helper method để clone support request
    private SupportRequest cloneSupportRequest(SupportRequest original) {
        return SupportRequest.builder()
                .supportId(original.getSupportId())
                .internId(original.getInternId())
                .supportType(original.getSupportType())
                .title(original.getTitle())
                .description(original.getDescription())
                .status(original.getStatus())
                .requestDate(original.getRequestDate())
                .processedBy(original.getProcessedBy())
                .processedDate(original.getProcessedDate())
                .response(original.getResponse())
                .build();
    }
}
