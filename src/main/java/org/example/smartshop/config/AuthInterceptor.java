package org.example.smartshop.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false); // false = don't create a new one if missing

        if (session != null && session.getAttribute("UserId") != null) {
            return true; // User is logged in, allow request
        }

        response.sendError(HttpStatus.UNAUTHORIZED.value(), "You must be logged in");
        return false; // Block request
    }
}
