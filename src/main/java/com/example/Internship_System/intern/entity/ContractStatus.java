package com.example.Internship_System.intern.entity;

public enum ContractStatus {
    NOT_UPLOAD("Not Uploaded"),
    UPLOAD("Uploaded");

    private final String displayName;

    ContractStatus(String displayName) {
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
