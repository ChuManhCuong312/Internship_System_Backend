package com.example.Internship_System.intern.dto;

import com.example.Internship_System.intern.entity.InternProfile;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InternProfileWithPhoneDTO {
    private InternProfile internProfile;
    private String phone;
@SuppressWarnings("unused")
    public InternProfileWithPhoneDTO() {}

    public InternProfileWithPhoneDTO(InternProfile internProfile, String phone) {
        this.internProfile = internProfile;
        this.phone = phone;
    }

}
