package com.example.Internship_System.auth.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId; // Use Integer to match SQL INT

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    // ============================
    // Relationships
    // ============================

    // Many Users belong to one Role (since users table has role_id as FK)
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    // Many-to-Many with Permissions through role_permissions table
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    // ============================
    // Getters and Setters
    // ============================

    public Integer getRoleId() {return roleId;}
    public void setRoleId(Integer roleId) {this.roleId = roleId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Set<User> getUsers() {return users;}
    public void setUsers(Set<User> users) {this.users = users;}

    public Set<Permission> getPermissions() {return permissions;}
    public void setPermissions(Set<Permission> permissions) {this.permissions = permissions;}
}
