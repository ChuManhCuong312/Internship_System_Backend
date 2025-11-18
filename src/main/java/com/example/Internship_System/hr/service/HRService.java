package com.example.Internship_System.hr.service;

import com.example.Internship_System.hr.dto.CandidateDTO;
import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.repository.HRRepository;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Internship_System.repository.InternLogRepository;
import com.example.Internship_System.intern.entity.InternLog;
import java.time.LocalDateTime;

import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.auth.entity.User;

import java.util.Optional;

@Service
public class HRService {

    @Autowired
    private HRRepository repository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private InternLogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<HRInternDTO> getAllInternsForHR(Pageable pageable) {
        return repository.findAllInternProfilesForHR(null, null, null, pageable);
    }

    public Page<HRInternDTO> searchInterns(String searchTerm, String major, String status, Pageable pageable) {
        return repository.findAllInternProfilesForHR(searchTerm, major, status, pageable);
    }

    public void updateStatus(int internId, String status, String rejectionReason) {
        InternProfile intern = repository.findById(internId)
                .orElseThrow(() -> new RuntimeException("Intern profile not found with id: " + internId));

        intern.setStatus(status);

        if ("REJECTED".equalsIgnoreCase(status)) {
            intern.setRejectionReason(rejectionReason);
        } else {
            intern.setRejectionReason(null);
        }

        repository.save(intern);

        // Lấy thông tin User từ userId
        User user = userRepository.findById(intern.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fullName = user.getFullName();
        String email = user.getEmail();

        // Gửi email
        String subject;
        String body;

        if ("APPROVED".equalsIgnoreCase(status)) {
            subject = "Thông báo duyệt hồ sơ Internship System";
            body = "Xin chào " + fullName + ",\n\n"
                    + "Hồ sơ của bạn đã được duyệt thành công.\n"
                    + "Bạn có thể tiếp tục quy trình thực tập.\n\nTrân trọng.";
        } else if ("REJECTED".equalsIgnoreCase(status)) {
            subject = "Thông báo từ chối hồ sơ Internship System";
            body = "Xin chào " + fullName + ",\n\n"
                    + "Rất tiếc, hồ sơ của bạn đã bị từ chối.\n"
                    + "Lý do: " + rejectionReason + "\n\n"
                    + "Bạn có thể chỉnh sửa và nộp lại hồ sơ.\n\nTrân trọng.";
        } else {
            subject = "Cập nhật trạng thái hồ sơ Internship System";
            body = "Xin chào " + fullName + ",\n\n"
                    + "Trạng thái hồ sơ của bạn đã được cập nhật thành: " + status + ".";
        }

        emailService.sendEmail(email, subject, body);

        // Tạo log
        InternLog log = new InternLog();
        log.setUserId(intern.getUserId());
        log.setMessage("Trạng thái hồ sơ đã được cập nhật: " + status);
        log.setCreatedAt(LocalDateTime.now());
        logRepository.save(log);
    }

    public void createInternProfileForUser(User user, InternProfile profileData) {
        profileData.setUserId(user.getUserId());
        profileData.setStatus("NO_FILE");
        repository.save(profileData);
    }

    public Page<CandidateDTO> getInternCandidatesWithoutProfile(Pageable pageable) {
        return repository.findInternUsersWithoutProfile(pageable);
    }

    public void updateInternProfile(int internId, InternProfile updatedProfile) {
        InternProfile existing = repository.findById(internId)
                .orElseThrow(() -> new RuntimeException("Intern profile not found with id: " + internId));

        // Chỉ cho phép sửa khi status = APPROVED
        if (!"APPROVED".equalsIgnoreCase(existing.getStatus())) {
            throw new RuntimeException("Chỉ được sửa hồ sơ đã duyệt");
        }

        if (updatedProfile.getSchool() != null) existing.setSchool(updatedProfile.getSchool());
        if (updatedProfile.getMajor() != null) existing.setMajor(updatedProfile.getMajor());
        if (updatedProfile.getDob() != null) existing.setDob(updatedProfile.getDob());
        if (updatedProfile.getAddress() != null) existing.setAddress(updatedProfile.getAddress());
        if (updatedProfile.getGender() != null) existing.setGender(updatedProfile.getGender());
        if (updatedProfile.getGpa() > 0) existing.setGpa(updatedProfile.getGpa());

        repository.save(existing);
    }
}


