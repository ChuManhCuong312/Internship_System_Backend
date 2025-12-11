package com.example.Internship_System.hr.dto;

import java.util.List;

public class InternStatusBatchUpdateRequest {

    private List<Integer> internIds;
    private String status;
    private String rejectionReason;

    public InternStatusBatchUpdateRequest() {
    }

    public List<Integer> getInternIds() {
        return internIds;
    }

    public void setInternIds(List<Integer> internIds) {
        this.internIds = internIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
