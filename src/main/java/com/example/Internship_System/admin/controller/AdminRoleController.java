package com.example.Internship_System.admin.controller;

import com.example.Internship_System.auth.entity.Permission;
import com.example.Internship_System.auth.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/roles")
public class AdminRoleController {
    private final RoleService roleService;

    public AdminRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // 🔹 Assign permission
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{roleName}/permissions")
    public ResponseEntity<String> assignPermissionsToRole(
            @PathVariable String roleName,
            @RequestBody Set<String> permissionCodes) {

        roleService.assignPermissionsToRole(roleName, permissionCodes);
        return ResponseEntity.ok("Permissions assigned successfully");
    }

    // 🔹 Remove permission
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{roleName}/permissions")
    public ResponseEntity<String> removePermissionsFromRole(
            @PathVariable String roleName,
            @RequestBody Set<String> permissionCodes) {

        roleService.removePermissionsFromRole(roleName, permissionCodes);
        return ResponseEntity.ok("Permissions removed successfully");
    }

    // 🔹 View permissions of a role
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{roleName}/permissions")
    public ResponseEntity<Set<Permission>> getPermissions(@PathVariable String roleName) {
        return ResponseEntity.ok(roleService.getPermissionsByRole(roleName));
    }
}
