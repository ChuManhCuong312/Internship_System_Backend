package com.example.Internship_System.repository;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.auth.entity.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    @Query("""
    SELECT u FROM User u
    WHERE (:roleId IS NULL OR u.role.roleId = :roleId)
      AND (:status IS NULL OR u.status = :status)
      AND (
            :nameOrEmail IS NULL 
            OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :nameOrEmail, '%'))
            OR LOWER(u.email) LIKE LOWER(CONCAT('%', :nameOrEmail, '%'))
          )
    """)
    Page<User> searchUsers(@Param("roleId") Integer roleId,
                           @Param("status") UserStatus status,
                           @Param("nameOrEmail") String nameOrEmail,
                           Pageable pageable);
}
