package com.example.Internship_System.hr.dto;

import com.example.Internship_System.intern.entity.ContractStatus;
import com.example.Internship_System.intern.entity.InternConfirmStatus;

import java.time.LocalDateTime;

public class HRContractDTO {
    private Integer documentId;
    private Integer internId;
    private String fullName;
    private String phone;
    private String filePath;
    private ContractStatus contractStatus;
    private InternConfirmStatus internConfirmStatus;
    private LocalDateTime confirmAt;
    private LocalDateTime createdAt;
    private String note;

    public HRContractDTO() {}

    public HRContractDTO(Integer documentId, Integer internId, String fullName, String phone,
                        String filePath, ContractStatus contractStatus, 
                        InternConfirmStatus internConfirmStatus, LocalDateTime confirmAt,
                        LocalDateTime createdAt, String note) {
        this.documentId = documentId;
        this.internId = internId;
        this.fullName = fullName;
        this.phone = phone;
        this.filePath = filePath;
        this.contractStatus = contractStatus;
        this.internConfirmStatus = internConfirmStatus;
        this.confirmAt = confirmAt;
        this.createdAt = createdAt;
        this.note = note;
    }

    // Getters and Setters
    public Integer getDocumentId() { return documentId; }
    public void setDocumentId(Integer documentId) { this.documentId = documentId; }

    public Integer getInternId() { return internId; }
    public void setInternId(Integer internId) { this.internId = internId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public ContractStatus getContractStatus() { return contractStatus; }
    public void setContractStatus(ContractStatus contractStatus) { this.contractStatus = contractStatus; }

    public InternConfirmStatus getInternConfirmStatus() { return internConfirmStatus; }
    public void setInternConfirmStatus(InternConfirmStatus internConfirmStatus) { 
        this.internConfirmStatus = internConfirmStatus; 
    }

    public LocalDateTime getConfirmAt() { return confirmAt; }
    public void setConfirmAt(LocalDateTime confirmAt) { this.confirmAt = confirmAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}

