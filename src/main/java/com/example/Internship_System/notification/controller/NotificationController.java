package com.example.Internship_System.notification.controller;

import com.example.Internship_System.notification.entity.Notification;
import com.example.Internship_System.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    //READ notifications by intern id
    @GetMapping("/intern/{internId}")
    public ResponseEntity<?> getNotificationsByInternId(@PathVariable("internId") int internId) {
        try {
            List<Notification> notifications = notificationService.findByInternId(internId);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ unread notifications by intern id
    @GetMapping("/intern/{internId}/unread")
    public ResponseEntity<?> getUnreadNotificationsByInternId(@PathVariable("internId") int internId) {
        try {
            List<Notification> notifications = notificationService.findUnreadByInternId(internId);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ notification by id
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable("id") int id) {
        Optional<Notification> notification = notificationService.findById(id);
        return notification.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //UPDATE notification - mark as read
    @PutMapping("/{id}/mark-read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable("id") int id) {
        try {
            Notification notification = notificationService.markAsRead(id);
            if (notification != null) {
                return new ResponseEntity<>(notification, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //DELETE notification by id
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNotification(@PathVariable("id") int id) {
        try {
            Optional<Notification> notification = notificationService.findById(id);
            if (notification.isPresent()) {
                notificationService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
