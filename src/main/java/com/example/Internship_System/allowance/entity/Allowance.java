package com.example.Internship_System.allowance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
@SuppressWarnings("unused")
    public Allowance() {
    }

    public Allowance(int internId, String type, BigDecimal amount, LocalDate dateApplied, String note) {
        this.internId = internId;
        this.type = type;
        this.amount = amount;
        this.dateApplied = dateApplied;
        this.note = note;
    }

}
