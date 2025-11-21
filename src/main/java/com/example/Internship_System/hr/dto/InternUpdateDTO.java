package com.example.Internship_System.hr.dto;

import com.example.Internship_System.validation.MinimumAge;
import com.example.Internship_System.validation.ValidPhone;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class InternUpdateDTO {

    @Size(min = 2, max = 150, message = "Tên trường phải từ 2 đến 150 ký tự")
    private String school;

    @Size(min = 2, max = 150, message = "Tên ngành phải từ 2 đến 150 ký tự")
    private String major;

    @Past(message = "Ngày sinh phải ở quá khứ")
    @MinimumAge(value = 18, message = "Tuổi phải từ 18 trở lên")
    private LocalDate dob;

    @Size(min = 5, max = 255, message = "Địa chỉ phải từ 5 đến 255 ký tự")
    private String address;

    @Pattern(regexp = "^(MALE|FEMALE)?$", message = "Giới tính phải là MALE hoặc FEMALE")
    private String gender;

    @Positive(message = "GPA phải lớn hơn 0")
    @DecimalMax(value = "4.0", message = "GPA phải nhỏ hơn 4")
    private double gpa;

    @ValidPhone(message = "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số")
    private String phone;

    @Size(max = 255, message = "Đường dẫn file phải ngắn hơn 255 kí tự, định dạng .png hoặc .jpg")
    @Column(name = "intern_image_path")
    private String avatar;

    // Getters & Setters
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
