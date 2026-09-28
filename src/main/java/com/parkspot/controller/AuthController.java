package com.parkspot.controller;

import com.parkspot.config.AuthInterceptor;
import com.parkspot.dto.LoginRequest;
import com.parkspot.dto.LoginResponse;
import com.parkspot.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        LoginResponse user = authService.login(request);

        HttpSession oldSession = httpRequest.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();                 // always start a fresh session after login
        }
        httpRequest.getSession(true).setAttribute(AuthInterceptor.SESSION_USER, user.username());
        return user;
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Map.of("message", "Logged out successfully.");
    }

    // Protected by the interceptor: returns 401 when nobody is logged in
    @GetMapping("/me")
    public LoginResponse currentUser(HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getSession(false).getAttribute(AuthInterceptor.SESSION_USER);
        return authService.getProfile(username);
    }
}
