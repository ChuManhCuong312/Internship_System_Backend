package com.example.Internship_System.notification.service;

import com.example.Internship_System.notification.entity.Notification;
import com.example.Internship_System.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    public Notification save(Notification notification) {
        return repository.save(notification);
    }

    public List<Notification> findByInternId(int internId) {
        return repository.findByInternId(internId);
    }

    public List<Notification> findUnreadByInternId(int internId) {
        return repository.findByInternIdAndIsReadFalse(internId);
    }

    public Optional<Notification> findById(int id) {
        return repository.findById(id);
    }

    public Notification markAsRead(int notificationId) {
        Optional<Notification> notification = repository.findById(notificationId);
        if (notification.isPresent()) {
            Notification n = notification.get();
            n.setRead(true);
            return repository.save(n);
        }
        return null;
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public void createAllowanceNotification(int internId, String allowanceType, String amount) {
        String title = "Bạn có trợ cấp mới";
        String message = "Bạn vừa nhận được trợ cấp " + allowanceType + " với số tiền: " + amount + " VND";
        Notification notification = new Notification(internId, title, message, "ALLOWANCE");
        save(notification);
    }


    public void createProfileStatusNotification(int internId, String status, String rejectionReason) {
        String upperStatus = status != null ? status.toUpperCase() : "";


        String title;
        String message;


        if ("APPROVED".equals(upperStatus)) {
            title = "Hồ sơ thực tập đã được duyệt";
            message = "Hồ sơ của bạn đã được duyệt thành công.";
        } else if ("REJECTED".equals(upperStatus)) {
            title = "Hồ sơ thực tập đã bị từ chối";
            message = "Hồ sơ của ban đã bị từ chối. Lý do: " + rejectionReason;
        } else {
            title = "Trạng thái hồ sơ thực tập được cập nhật";
            message = "Trạng thái hồ sơ của bạn đã được cập nhật thành: " + status + ".";
        }


        Notification notification = new Notification(internId, title, message, "PROFILE_STATUS");
        save(notification);
    }


    public void createProfileUpdatedNotification(int internId) {
        String title = "Hồ sơ thực tập đã được cập nhật";
        String message = "Hồ sơ của bạn đã được HR cập nhật. Vui lòng kiểm tra lại thông tin.";
        Notification notification = new Notification(internId, title, message, "PROFILE_UPDATE");
        save(notification);
    }


    public void createLeaveApprovedNotification(int internId, LocalDate startDate, LocalDate endDate) {
        String title = "Đơn nghỉ phép đã được duyệt";
        String message = "Đơn nghỉ phép từ " + startDate + " đến " + endDate + " đã được HR duyệt.";
        Notification notification = new Notification(internId, title, message, "LEAVE_REQUEST");
        save(notification);
    }


    public void createLeaveRejectedNotification(int internId, LocalDate startDate, LocalDate endDate, String rejectionReason) {
        String title = "Đơn nghỉ phép đã bị từ chối";
        String message = "Đơn nghỉ phép từ " + startDate + " đến " + endDate + " đã bị HR từ chối. Lý do: " + rejectionReason;
        Notification notification = new Notification(internId, title, message, "LEAVE_REQUEST");
        save(notification);
    }
}
