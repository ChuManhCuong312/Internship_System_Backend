package com.example.Internship_System.allowance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    public AllowanceDTO(int internId, String internName, String type, BigDecimal amount, LocalDate dateApplied, String note) {
        this.internId = internId;
        this.internName = internName;
        this.type = type;
        this.amount = amount;
        this.dateApplied = dateApplied;
        this.note = note;
    }

    public int getAllowanceId() {
        return allowanceId;
    }

    public void setAllowanceId(int allowanceId) {
        this.allowanceId = allowanceId;
    }

    public int getInternId() {
        return internId;
    }

    public void setInternId(int internId) {
        this.internId = internId;
    }

    public String getInternName() {
        return internName;
    }

    public void setInternName(String internName) {
        this.internName = internName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(LocalDate dateApplied) {
        this.dateApplied = dateApplied;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
