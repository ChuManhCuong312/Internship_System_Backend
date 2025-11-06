package com.example.Internship_System.mentor.entity;

import com.example.Internship_System.auth.entity.User; // đổi nếu User nằm package khác
import jakarta.persistence.*;

@Entity
@Table(name = "mentor_users")
public class MentorUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_id")
    private Long mentorId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100)
    private String department;

    @Column(length = 255)
    private String expertise;

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
}
