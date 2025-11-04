package com.example.Internship_System.auth.service;

import com.example.Internship_System.auth.entity.Permission;
import com.example.Internship_System.auth.entity.Role;
import com.example.Internship_System.repository.PermissionRepository;
import com.example.Internship_System.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository,PermissionRepository permissionRepository){
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    // Get role by name
    public Optional<Role> getRoleByName(String name){
        return roleRepository.findByName(name);
    }

    // Assign permissions to a role
    public Role assignPermissionsToRole(String roleName, Set<String> permissionCodes){
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Permission> permissions = new HashSet<>();
        for (String permCode : permissionCodes){
            Permission perm = permissionRepository.findByCode(permCode)
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + permCode));
            permissions.add(perm);
        }

        role.setPermissions(permissions);
        return roleRepository.save(role);
    }
}
