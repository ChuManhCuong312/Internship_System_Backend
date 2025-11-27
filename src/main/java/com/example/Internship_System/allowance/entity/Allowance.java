package com.example.Internship_System.allowance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "allowances")
public class Allowance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allowance_id", nullable = false)
    private int allowanceId;

    @Column(name = "intern_id",nullable = false)
    @NotNull(message = "InternID không thể null")
    private int internId;

    @Column(name = "type",nullable = false)
    @NotNull(message = "Type không thể null")
    private String type;

    @Column(name = "amount", nullable = false )
    @DecimalMin(value = "0.0", inclusive = false, message = "Trợ cấp phải lớn hơn 0")
    private BigDecimal amount;

    @Column(name = "date_applied")
    private LocalDate dateApplied;

    @Column(name = "note")
    private String note;

    public Allowance() {
    }

    public Allowance(int internId, String type, BigDecimal amount, LocalDate dateApplied, String note) {
        this.internId = internId;
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
