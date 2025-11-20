package com.example.Internship_System.intern.dto;

import com.example.Internship_System.intern.entity.InternProfile;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InternProfileWithPhoneDTO {
    private InternProfile internProfile;
    private String phone;

    public InternProfileWithPhoneDTO() {}

    public InternProfileWithPhoneDTO(InternProfile internProfile, String phone) {
        this.internProfile = internProfile;
        this.phone = phone;
    }

    public InternProfile getInternProfile() {
        return internProfile;
    }

    public void setInternProfile(InternProfile internProfile) {
        this.internProfile = internProfile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
