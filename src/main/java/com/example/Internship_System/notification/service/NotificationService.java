package com.example.Internship_System.notification.service;

import com.example.Internship_System.notification.entity.Notification;
import com.example.Internship_System.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
