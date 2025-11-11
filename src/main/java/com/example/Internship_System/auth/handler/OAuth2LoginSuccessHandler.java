package com.example.Internship_System.auth.handler;

import com.example.Internship_System.config.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

        var oAuth2User = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();
        String email =   oAuth2User.getAttribute("email");
        if (email == null) {
            email = oAuth2User.getAttribute("login") + "@github.com"; // fallback for GitHub
        }

        // Extract role from authorities
        String rawRole = authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("INTERN");
        // Normalize any OAuth2 role to INTERN
        String role = rawRole.equalsIgnoreCase("OAUTH2_USER") ? "INTERN" : rawRole;
        String token = jwtUtils.generateToken(email, role);

        // Create a cookie for the token
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true) // use HTTPS in production
                .path("/")
                .maxAge(30 * 60) // 30 minutes
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        // Redirect back to frontend with token
        String redirectUrl = "http://localhost:5173/oauth-success";
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
