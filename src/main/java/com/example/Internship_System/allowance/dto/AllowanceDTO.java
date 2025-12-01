package com.example.Internship_System.allowance.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class AllowanceDTO {
    private int allowanceId;
    private int internId;
    private String internName;
    private String type;
    private BigDecimal amount;
    private LocalDate dateApplied;
    private String note;

    public AllowanceDTO() {
    }
@SuppressWarnings("unused")
    public AllowanceDTO(int internId, String internName, String type, BigDecimal amount, LocalDate dateApplied, String note) {
        this.internId = internId;
        this.internName = internName;
        this.type = type;
        this.amount = amount;
        this.dateApplied = dateApplied;
        this.note = note;
    }

}
