package com.example.Internship_System.mentor.entity;

import com.example.Internship_System.auth.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "mentor_users")
public class MentorUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_id")
    private Integer mentorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")  // FK in users table
    private User user;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "expertise", length = 255)
    private String expertise;

    public MentorUser() {}

    public MentorUser(User user, String department, String expertise){
        this.user = user;
        this.department = department;
        this.expertise = expertise;
    }

    public Integer getMentorId() {return mentorId;}
    public void setMentorId(Integer mentorId) {this.mentorId = mentorId;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}

    public String getExpertise() {return expertise;}
    public void setExpertise(String expertise) {this.expertise = expertise;}
}
