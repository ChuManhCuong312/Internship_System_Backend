package com.example.Internship_System.intern.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDTO {
    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;


    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;


    @NotBlank(message = "Lý do nghỉ phép không được để trống")
    @Size(min = 10, max = 255, message = "Lý do phải từ 10 đến 255 ký tự")
    private String reason;
}

