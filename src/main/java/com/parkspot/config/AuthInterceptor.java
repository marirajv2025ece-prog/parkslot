package com.parkspot.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

/**
 * Login check. Every /api/** request must have a logged-in session,
 * otherwise a 401 JSON response is returned (the frontend then opens login.html).
 */
public class AuthInterceptor implements HandlerInterceptor {

    public static final String SESSION_USER = "loggedInUser";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SESSION_USER) != null) {
            return true;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"timestamp\":\"" + LocalDateTime.now().withNano(0)
                + "\",\"status\":401,\"message\":\"Please log in to continue.\"}");
        return false;
    }
}
