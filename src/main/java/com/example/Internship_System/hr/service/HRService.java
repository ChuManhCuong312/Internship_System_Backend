package com.example.Internship_System.hr.service;

import com.example.Internship_System.hr.dto.CandidateDTO;
import com.example.Internship_System.hr.dto.HRInternDTO;
import com.example.Internship_System.repository.HRRepository;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.utils.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Internship_System.repository.InternLogRepository;
import com.example.Internship_System.intern.entity.InternLog;
import com.example.Internship_System.hr.dto.InternUpdateDTO;
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
        log.setInternId(intern.getInternId());
        log.setDetails("Trạng thái hồ sơ đã được cập nhật: " + status);
        log.setCreatedAt(LocalDateTime.now());
        logRepository.save(log);
    }

    @Transactional
    public void createInternProfileForUser(int userId, String phone, InternProfile profileData) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (phone != null && !phone.trim().isEmpty()) {
            String newPhone = phone.trim();

            if (!newPhone.equals(existingUser.getPhone())) {
                if (userRepository.existsByPhone(newPhone)) {
                    throw new RuntimeException("Số điện thoại đã tồn tại trong hệ thống");
                }

                existingUser.setPhone(newPhone);
                userRepository.saveAndFlush(existingUser);
            }
        }

        profileData.setUserId(userId);
        profileData.setStatus("NO_FILE");
        repository.save(profileData);
    }

    public Page<CandidateDTO> getInternCandidatesWithoutProfile(Pageable pageable) {
        return repository.findInternUsersWithoutProfile(pageable);
    }

    @Transactional
    public void updateInternProfile(int internId, InternUpdateDTO dto) {
        InternProfile existing = repository.findById(internId)
                .orElseThrow(() -> new RuntimeException("Intern profile not found with id: " + internId));

        if (!"APPROVED".equalsIgnoreCase(existing.getStatus())) {
            throw new RuntimeException("Chỉ được sửa hồ sơ đã duyệt");
        }

        if (dto.getSchool() != null) existing.setSchool(dto.getSchool());
        if (dto.getMajor() != null) existing.setMajor(dto.getMajor());
        if (dto.getDob() != null) existing.setDob(dto.getDob());
        if (dto.getAddress() != null) existing.setAddress(dto.getAddress());
        if (dto.getGender() != null) existing.setGender(dto.getGender());
        if (dto.getGpa() > 0) existing.setGpa(dto.getGpa());

        if (dto.getPhone() != null) {
            User user = userRepository.findById(existing.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!dto.getPhone().equals(user.getPhone())) {
                if (userRepository.existsByPhone(dto.getPhone())) {
                    throw new RuntimeException("Số điện thoại đã tồn tại trong hệ thống");
                }

                user.setPhone(dto.getPhone());
                userRepository.saveAndFlush(user);
            }
        }

        repository.saveAndFlush(existing);
    }
}



