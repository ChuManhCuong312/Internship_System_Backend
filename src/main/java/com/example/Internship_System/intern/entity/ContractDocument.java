package com.example.Internship_System.intern.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "contract_documents")
public class ContractDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Integer documentId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "intern_id", nullable = false)
    private InternProfile intern;

    @Size(max = 255, message = "File path name must not exceed 255 characters")
    @Column(name = "file_path")
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_status", columnDefinition = "ENUM('NOT_UPLOAD','UPLOAD') DEFAULT 'NOT_UPLOAD'")
    private ContractStatus contractStatus = ContractStatus.NOT_UPLOAD;

    @Enumerated(EnumType.STRING)
    @Column(name = "intern_confirm_status", columnDefinition = "ENUM('APPROVED','PENDING') DEFAULT 'PENDING'")
    private InternConfirmStatus internConfirmStatus = InternConfirmStatus.PENDING;

    @Column(name = "confirm_at")
    private LocalDateTime confirmAt;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    public ContractDocument() {}

    public ContractDocument (InternProfile intern, String filePath, LocalDateTime confirmAt, String note){
        this.intern = intern;
        this.filePath = filePath;
        this.confirmAt = confirmAt;
        this.note = note;
    }

    public Integer getDocumentId() {return documentId;}
    public void setDocumentId(Integer documentId) {this.documentId = documentId;}

    public InternProfile getIntern() {return intern;}
    public void setIntern(InternProfile intern) {this.intern = intern;}

    public String getFilePath() {return filePath;}
    public void setFilePath(String filePath) {this.filePath = filePath;}

    public ContractStatus getContractStatus() {return contractStatus;}
    public void setContractStatus(ContractStatus contractStatus) {this.contractStatus = contractStatus;}

    public InternConfirmStatus getInternConfirmStatus() {return internConfirmStatus;}
    public void setInternConfirmStatus(InternConfirmStatus internConfirmStatus) {this.internConfirmStatus = internConfirmStatus;}

    public LocalDateTime getConfirmAt() {return confirmAt;}
    public void setConfirmAt(LocalDateTime confirmAt) {this.confirmAt = confirmAt;}

    public String getNote() {return note;}
    public void setNote(String note) {this.note = note;}
}
