package com.example.Internship_System.support.dto;

import com.example.Internship_System.support.entity.SupportStatus;
import com.example.Internship_System.support.entity.SupportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

// DTO để tạo support request mới
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSupportRequestDTO {

    @NotNull(message = "Support type không được để trống")
    private SupportType supportType;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề không được vượt quá 200 ký tự")
    private String title;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;
}

// DTO để trả về thông tin support request với thông tin bổ sung
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class SupportRequestResponseDTO {
    private Integer supportId;
    private Integer internId;
    private String internName;
    private String internEmail;
    private SupportType supportType;
    private String title;
    private String description;
    private SupportStatus status;
    private LocalDateTime requestDate;
    private Integer processedBy;
    private String processedByName;
    private LocalDateTime processedDate;
    private String response;
    private List<SupportStatusHistoryDTO> history;
}

// DTO để update status
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class UpdateSupportStatusDTO {
    private String response;
}

// DTO cho lịch sử thay đổi status
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class SupportStatusHistoryDTO {
    private Integer historyId;
    private Integer supportId;
    private SupportStatus oldStatus;
    private SupportStatus newStatus;
    private Integer changedBy;
    private String changedByName;
    private LocalDateTime changeDate;
    private String remarks;
}
