package com.example.Internship_System.intern.entity;

public enum InternConfirmStatus {
    PENDING("Pending"),
    APPROVED("Approved");

    private final String displayName;

    InternConfirmStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
