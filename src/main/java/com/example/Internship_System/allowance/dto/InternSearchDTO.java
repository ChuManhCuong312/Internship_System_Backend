package com.example.Internship_System.allowance.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InternSearchDTO {
    private int internId;
    private String fullName;
@SuppressWarnings("unused")
    public InternSearchDTO() {
    }
@SuppressWarnings("unused")
    public InternSearchDTO(int internId, String fullName) {
        this.internId = internId;
        this.fullName = fullName;
    }
}
