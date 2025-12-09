package com.example.Internship_System.auth.handler;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.auth.entity.UserStatus;
import com.example.Internship_System.config.JwtUtils;
import com.example.Internship_System.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public OAuth2LoginSuccessHandler(JwtUtils jwtUtils,UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        var oauthToken = (org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) authentication;
        var attributes = oauthToken.getPrincipal().getAttributes();

        String email = (String) attributes.get("email");
        String fullNameAttr = (String) attributes.get("name");

        // Lookup user in DB FIRST
        Optional<User> existingUser = userRepository.findByEmail(email);

        // NEW — check rejection block
        if (existingUser.isPresent() && existingUser.get().getStatus() == UserStatus.REJECTED) {
            // Redirect to a proper error page
            getRedirectStrategy().sendRedirect(request, response,
                    "http://localhost:5173/oauth-failed");
            return;
        }

        // Extract role as before
        String rawRole = authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("INTERN");
        String role = rawRole.equalsIgnoreCase("OAUTH2_USER") ? "INTERN" : rawRole;

        Integer userId = existingUser.map(User::getUserId).orElse(null);
        String fullName = existingUser.map(User::getFullName).orElse(fullNameAttr != null ? fullNameAttr : "Unknown");

        // Generate JWT normally
        String token = jwtUtils.generateToken(email, role, userId, fullName);

        // Set cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);

        // Redirect to frontend success
        String redirectUrl = "http://localhost:5173/oauth-success?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }


}
