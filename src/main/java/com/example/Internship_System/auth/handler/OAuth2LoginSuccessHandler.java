package com.example.Internship_System.auth.handler;

import com.example.Internship_System.config.JwtUtils;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    public OAuth2LoginSuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String email = authentication.getName();
        // Extract role from authorities
        String rawRole = authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("INTERN");
        // Normalize any OAuth2 role to INTERN
        String role = rawRole.equalsIgnoreCase("OAUTH2_USER") ? "INTERN" : rawRole;
        String token = jwtUtils.generateToken(email, role);

        // Create a cookie for the token
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        cookie.setMaxAge(24 * 60 * 60); // 1 day
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
        // Redirect back to frontend with token
        String redirectUrl = "http://localhost:5173/oauth-success?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
