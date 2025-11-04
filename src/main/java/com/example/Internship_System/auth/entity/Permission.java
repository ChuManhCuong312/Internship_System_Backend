package com.example.Internship_System.auth.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer permissionId; // Use Integer to match SQL INT

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "description", length = 255)
    private String description;

    // ============================
    // Relationships
    // ============================

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    // ============================
    // Getters and Setters
    // ============================

    public Integer getPermissionId() {return permissionId;}
    public void setPermissionId(Integer permissionId) {this.permissionId = permissionId;}

    public String getCode() {return code;}
    public void setCode(String code) {this.code = code;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Set<Role> getRoles() {return roles;}
    public void setRoles(Set<Role> roles) {this.roles = roles;}
}
