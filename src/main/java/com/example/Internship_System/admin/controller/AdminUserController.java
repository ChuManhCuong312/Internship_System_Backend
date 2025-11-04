package com.example.Internship_System.admin.controller;

import com.example.Internship_System.auth.dto.RegisterRequest;
import com.example.Internship_System.auth.entity.Role;
import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.auth.entity.UserStatus;
import com.example.Internship_System.auth.service.AuthService;
import com.example.Internship_System.auth.service.RoleService;
import com.example.Internship_System.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class AdminUserController {

    private final AuthService authService;
    private final RoleService roleService;
    private final UserRepository userRepository;

    public AdminUserController(AuthService authService,RoleService roleService,UserRepository userRepository){
        this.authService = authService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    // Create user(Admin only)
    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest request, @RequestParam String roleName){
        if (userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(authService.encodePassWord(request.getPassword()));

        // Set status: INTERN INACTIVE approval, other active immediately
        if(roleName.equalsIgnoreCase("INTERN")){
            user.setStatus(UserStatus.INACTIVE);
        }else{
            user.setStatus(UserStatus.ACTIVE);
        }

        // Asign role
        Optional<Role> roleOpt = roleService.getRoleByName(roleName);
        if (roleOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Role not found: " + roleName);
        }
        user.setRole(roleOpt.get());

        userRepository.save(user);
        return ResponseEntity.ok("User created successfully with role "+ roleName);
    }

    // 🔹 Approve intern (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{userId}")
    public ResponseEntity<?> approveIntern(@PathVariable Integer userId) {
        return userRepository.findById(Long.valueOf(userId)) // ✅ match repository's Long ID type
                .map(user -> {
                    if (user.getStatus() != UserStatus.PENDING_APPROVAL) {
                        return ResponseEntity.badRequest().body("User must be PENDING_APPROVAL before approval.");
                    }

                    user.setStatus(UserStatus.ACTIVE);

                    // Assign role "INTERN"
                    var internRole = roleService.getRoleByName("INTERN");
                    if (internRole.isPresent()) {
                        user.setRole(internRole.get());
                    } else {
                        return ResponseEntity.badRequest().body("INTERN role not found.");
                    }

                    userRepository.save(user);
                    return ResponseEntity.ok("Intern approved and activated successfully.");
                })
                .orElse(ResponseEntity.badRequest().body("User not found."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject/{userId}")
    public ResponseEntity<?> rejectIntern(@PathVariable Integer userId) {
        return userRepository.findById(Long.valueOf(userId))
                .map(user -> {
                    if (user.getStatus() != UserStatus.PENDING_APPROVAL) {
                        return ResponseEntity.badRequest().body("User must be PENDING_APPROVAL before rejection.");
                    }

                    user.setStatus(UserStatus.REJECTED);
                    userRepository.save(user);
                    return ResponseEntity.ok("Intern registration rejected successfully.");
                })
                .orElse(ResponseEntity.badRequest().body("User not found."));
    }
}
