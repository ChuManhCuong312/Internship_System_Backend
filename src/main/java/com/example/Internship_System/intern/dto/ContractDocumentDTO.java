package com.example.Internship_System.intern.dto;

import com.example.Internship_System.intern.entity.ContractStatus;
import com.example.Internship_System.intern.entity.InternConfirmStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@SuppressWarnings("unused")
public class ContractDocumentDTO {

    private Integer documentId;

    @NotNull(message = "Intern ID is required")
    private Integer internId;

    @Size(max = 255, message = "File path must not exceed 255 characters")
    private String filePath;

    private ContractStatus contractStatus;

    private InternConfirmStatus internConfirmStatus;

    private LocalDateTime confirmAt;

    private String note;
@SuppressWarnings("unused")
    public ContractDocumentDTO() {}
@SuppressWarnings("unused")
    public ContractDocumentDTO(Integer documentId, Integer internId, String filePath,
                               ContractStatus contractStatus, InternConfirmStatus internConfirmStatus,
                               LocalDateTime confirmAt, String note) {
        this.documentId = documentId;
        this.internId = internId;
        this.filePath = filePath;
        this.contractStatus = contractStatus;
        this.internConfirmStatus = internConfirmStatus;
        this.confirmAt = confirmAt;
        this.note = note;
    }

}