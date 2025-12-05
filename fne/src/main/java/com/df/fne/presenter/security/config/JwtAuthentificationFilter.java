package com.df.fne.presenter.security.config;


import com.df.fne.core.exceptions.BadRequestException;
import com.df.fne.presenter.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Component
@RequiredArgsConstructor
public class JwtAuthentificationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
            "/api/auth/login",
            "/api/auth/register"
    );

    private static final List<String> ALLOWED_ORIGIN = Arrays.asList("http://localhost:5173",
            "http://localhost:8087");

    private boolean isPublicEndpoint(String servletPath) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(endpoint ->
                servletPath.startsWith(endpoint) ||
                        servletPath.matches(endpoint.replace("*", ".*"))
        );
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String servletPath = request.getServletPath();

            if (isPublicEndpoint(servletPath)) {
                filterChain.doFilter(request, response);
                return;
            }

            //Bloque tous les appels en dehors de ALLOWED_ORIGIN
        /*String origin = request.getHeader("Origin");
        System.out.println("origin "+origin);
            if (origin == null || !ALLOWED_ORIGIN.contains(origin)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden origin");
            return;
        }*/


            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final String jwt;
            final String userEmail;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            userEmail = jwtService.extractEmail(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            sendError(response, "Le token a expiré", HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException ex) {
            // Capture les erreurs métier et renvoie une réponse 400
            sendError(response, ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            sendError(response, "Échec de l'authentification", HttpStatus.UNAUTHORIZED);
        }

    }

    private void sendError(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status.value());
        response.getWriter().write(
                String.format("{\"status\":false,\"message\":\"%s\",\"code\":%d}",
                        message, status.value())
        );
    }

}
