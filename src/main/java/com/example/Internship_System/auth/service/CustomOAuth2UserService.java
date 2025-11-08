package com.example.Internship_System.auth.service;

import com.example.Internship_System.auth.entity.Role;
import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.auth.entity.UserStatus;
import com.example.Internship_System.repository.RoleRepository;
import com.example.Internship_System.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = null;
        String name = null;

        if ("google".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
        } else if ("github".equals(registrationId)) {
            name = oAuth2User.getAttribute("name");
            // GitHub sometimes returns null for "email" unless explicitly requested
            email = oAuth2User.getAttribute("email");
            if (email == null) {
                email = oAuth2User.getAttribute("login") + "@github.com";
            }
        }

        if (email == null) {
            throw new RuntimeException("Cannot retrieve email from OAuth2 provider: " + registrationId);
        }

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(name != null ? name : email);
            newUser.setStatus(UserStatus.PENDING_APPROVAL);

            roleRepository.findByName("INTERN").ifPresent(newUser::setRole);

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            newUser.setPasswordHash(encoder.encode("OAUTH_" + UUID.randomUUID()));

            userRepository.save(newUser);
        }

        return oAuth2User;
    }
}