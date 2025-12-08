package com.ashish.farm.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SpaWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        
        // LOGIC: 
        // 1. Do not touch API requests (starts with /api)
        // 2. Do not touch static files (javascript, css, images - usually have a dot extension)
        // 3. Everything else? Forward it to index.html so React can load.
        
        if (!path.startsWith("/api") && !path.contains(".") && path.matches("/.*")) {
            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}