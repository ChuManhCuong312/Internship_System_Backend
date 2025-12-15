package com.example.Internship_System.notification.repository;

import com.example.Internship_System.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByInternId(int internId);
    List<Notification> findByInternIdAndIsReadFalse(int internId);
    List<Notification> findByInternIdOrderByCreatedAtDesc(int internId);
}
