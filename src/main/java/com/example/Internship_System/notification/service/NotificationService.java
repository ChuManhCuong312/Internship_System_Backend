package com.example.Internship_System.notification.service;

import com.example.Internship_System.notification.entity.Notification;
import com.example.Internship_System.notification.repository.NotificationRepository;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.repository.ProgramRepository;
import com.example.Internship_System.repository.TeamRepository;
import com.example.Internship_System.team.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProgramRepository programRepository;

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


    @Async
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


        Notification notification = new Notification(internId, title, message, "SYSTEM");
        save(notification);
    }


    @Async
    public void createProfileUpdatedNotification(int internId) {
        String title = "Hồ sơ thực tập đã được cập nhật";
        String message = "Hồ sơ của bạn đã được cập nhật. Vui lòng kiểm tra lại thông tin.";
        Notification notification = new Notification(internId, title, message, "SYSTEM");
        save(notification);
    }


    public void createLeaveApprovedNotification(int internId, LocalDate startDate, LocalDate endDate) {
        String title = "Đơn nghỉ phép đã được duyệt";
        String message = "Đơn nghỉ phép từ " + startDate + " đến " + endDate + " đã được HR duyệt.";
        Notification notification = new Notification(internId, title, message, "LEAVE");
        save(notification);
    }


    public void createLeaveRejectedNotification(int internId, LocalDate startDate, LocalDate endDate, String rejectionReason) {
        String title = "Đơn nghỉ phép đã bị từ chối";
        String message = "Đơn nghỉ phép từ " + startDate + " đến " + endDate + " đã bị HR từ chối. Lý do: " + rejectionReason;
        Notification notification = new Notification(internId, title, message, "LEAVE");
        save(notification);
    }

    public void createInternAddedToTeamNotification(Integer internId, Integer programId, Integer teamId) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình"));

        String programName = program.getName();

        List<Team> teams =
                teamRepository.findByProgramProgramIdOrderByTeamIdAsc(programId);

        int teamNumber = -1;
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getTeamId().equals(teamId)) {
                teamNumber = i + 1;
                break;
            }
        }

        String title = "Bạn đã được thêm vào team";
        String message = "Bạn đã được thêm vào Team " + teamNumber +
                " của chương trình \"" + programName + "\".";

        Notification notification = new Notification(internId, title, message, "TEAM");

        save(notification);
    }

    public void createInternRemovedFromTeamNotification(Integer internId, Integer programId, Integer teamId) {
        // 🔹 Get program name
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình"));

        String programName = program.getName();

        // 🔹 Determine team order inside program
        List<Team> teams =
                teamRepository.findByProgramProgramIdOrderByTeamIdAsc(programId);

        int teamNumber = -1;
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getTeamId().equals(teamId)) {
                teamNumber = i + 1;
                break;
            }
        }

        String title = "Bạn đã bị xóa khỏi team";
        String message = "Bạn đã bị xóa khỏi Team " + teamNumber +
                " của chương trình \"" + programName + "\".";

        Notification notification =
                new Notification(internId, title, message, "TEAM");

        save(notification);
    }

    public void createTeamDeletedNotificationForIntern(Integer internId, Integer programId, Integer teamId) {
        // Get program name
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình"));

        String programName = program.getName();

        // Determine team number inside program
        List<Team> teams =
                teamRepository.findByProgramProgramIdOrderByTeamIdAsc(programId);

        int teamNumber = -1;
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getTeamId().equals(teamId)) {
                teamNumber = i + 1;
                break;
            }
        }

        String title = "Team đã bị giải tán";
        String message = "Bạn đã bị xóa khỏi Team " + teamNumber +
                " của chương trình \"" + programName + "\" do team bị giải tán.";

        Notification notification =
                new Notification(internId, title, message, "TEAM");

        save(notification);
    }

    public void createProgramDeletedNotification(Integer internId, String programName) {
        String title = "Chương trình đã bị xóa";
        String message = "Chương trình thực tập \"" + programName +
                "\" đã bị xóa. Bạn không còn thuộc chương trình này.";

        Notification notification =
                new Notification(internId, title, message, "PROGRAM");

        save(notification);
    }

}
