package com.example.Internship_System.intern.dto;

import com.example.Internship_System.intern.entity.ContractStatus;
import com.example.Internship_System.intern.entity.InternConfirmStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

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

    public ContractDocumentDTO() {}

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

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Integer getInternId() {
        return internId;
    }

    public void setInternId(Integer internId) {
        this.internId = internId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public InternConfirmStatus getInternConfirmStatus() {
        return internConfirmStatus;
    }

    public void setInternConfirmStatus(InternConfirmStatus internConfirmStatus) {
        this.internConfirmStatus = internConfirmStatus;
    }

    public LocalDateTime getConfirmAt() {
        return confirmAt;
    }

    public void setConfirmAt(LocalDateTime confirmAt) {
        this.confirmAt = confirmAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}