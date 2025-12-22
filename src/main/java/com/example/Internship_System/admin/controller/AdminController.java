package com.example.Internship_System.admin.controller;

import com.example.Internship_System.admin.dto.UserDTO;
import com.example.Internship_System.auth.entity.Role;
import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.auth.entity.UserStatus;
import com.example.Internship_System.repository.RoleRepository;
import com.example.Internship_System.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin/ManageUsers")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) String nameOrEmail
    ) {
        int pageIndex = page - 1;
        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "userId"));

        Page<User> userPage = userRepository.searchUsers(roleId, status, nameOrEmail, pageable);

        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getPhone(),
                        user.getStatus().name(),
                        user.getCreatedAt(),
                        user.getRole() != null ? user.getRole().getRoleId() : 0,
                        "" // không trả password ra client
                ))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("data", userDTOs);
        response.put("currentPage", userPage.getNumber() + 1);
        response.put("totalItems", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }
        if (userRepository.existsByPhone(userDTO.getPhone())) {
            return ResponseEntity.badRequest().body("Số điện thoại đã tồn tại!");
        }
        // 2. Lấy role theo roleId
        Optional<Role> roleOpt = roleRepository.findById(userDTO.getRoleId());
        if (roleOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Role không tồn tại!");
        }

        Role role = roleOpt.get();

        // 3. Tạo user entity
        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword())); // mã hóa password
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE); // default active
        user.setCreatedAt(LocalDateTime.now());

        // 4. Lưu vào DB
        userRepository.save(user);

        return ResponseEntity.ok("User tạo thành công!");
    }
    @PatchMapping("/activeUser/{userId}")
    public ResponseEntity<?> activeUser(@PathVariable Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User không tồn tại!");
        }

        User user = userOpt.get();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        return ResponseEntity.ok("Đã kích hoạt tài khoản thành công " );
    }

    @PatchMapping("/rejectUser/{userId}")
    public ResponseEntity<?> rejectUser(@PathVariable Integer userId) {
        // 1. Tìm user theo ID
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User không tồn tại!");
        }

        // 2. Cập nhật trạng thái
        User user = userOpt.get();
        user.setStatus(UserStatus.REJECTED);

        // 3. Lưu vào DB
        userRepository.save(user);

        // 4. Trả phản hồi
        return ResponseEntity.ok("Đã từ chối tài khoản thành công " );
    }

    @PatchMapping("/unlockUser/{userId}")
    public ResponseEntity<?> unlockUser(@PathVariable Integer userId) {
        // 1. Tìm user theo ID
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User không tồn tại!");
        }

        // 2. Cập nhật trạng thái
        User user = userOpt.get();
        user.setStatus(UserStatus.PENDING_APPROVAL);

        // 3. Lưu lại
        userRepository.save(user);

        // 4. Phản hồi thành công
        return ResponseEntity.ok("Đã mở khóa và chuyển trạng thái người dùng sang chờ duyệt");
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId, @RequestBody UserDTO userDTO) {
        // 1. Kiểm tra user tồn tại
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User không tồn tại!");
        }

        User user = userOpt.get();

        // 2. Kiểm tra email trùng (nếu người dùng đổi email)
        if (!user.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }
        if (!Objects.equals(user.getPhone(), userDTO.getPhone())
                && userDTO.getPhone() != null
                && userRepository.existsByPhone(userDTO.getPhone())) {
            return ResponseEntity.badRequest().body("Số điện thoại đã tồn tại!");
        }
        // 3. Cập nhật các trường cơ bản
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        // 4. Cập nhật role nếu có
        if (userDTO.getRoleId() != 0) {
            Optional<Role> roleOpt = roleRepository.findById(userDTO.getRoleId());
            if (roleOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Role không tồn tại!");
            }
            user.setRole(roleOpt.get());
        }

        //  Không thay đổi: createdAt, status và password

        // 6. Lưu lại vào DB
        userRepository.save(user);

        // 7. Phản hồi
        return ResponseEntity.ok("Cập nhật user thành công!");
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        try {
            // 1. Tìm user theo ID
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User không tồn tại!");
            }

            // 2. Thực hiện xóa user
            userRepository.deleteById(userId);

            // 3. Phản hồi thành công
            return ResponseEntity.ok("Đã xoá user thành công");

        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest()
                    .body("Thông tin người dùng nằm trong hệ thống, không được xóa để tránh mất dữ liệu!");
        } catch (Exception ex) {
            // Các lỗi khác
            return ResponseEntity.internalServerError()
                    .body("Đã xảy ra lỗi trong quá trình xóa người dùng!");
        }
    }

}
