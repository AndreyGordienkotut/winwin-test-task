package com.winwin.data_api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class InternalTokenFilter extends OncePerRequestFilter {


    @Value("${service.internal-token}")
    private String expectedToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (!request.getRequestURI().equals("/api/transform")
                || !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }
        String headerToken = request.getHeader("X-Internal-Token");

        if (headerToken == null || !headerToken.equals(expectedToken)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write("""
            {
            "timestamp": "%s",
             "status": 403,
            "message": "Invalid internal token"
            }
            """.formatted(LocalDateTime.now()));
            return;
        }

        filterChain.doFilter(request, response);
    }
}