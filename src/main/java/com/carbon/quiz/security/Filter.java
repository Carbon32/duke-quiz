package com.carbon.quiz.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Filter extends OncePerRequestFilter {
    private final TokenVerifier tokenVerifier;
    private final RateLimit rateLimitService;


    public Filter(TokenVerifier tokenVerifier, RateLimit rateLimitService) {
        this.tokenVerifier = tokenVerifier;
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || !request.getServletPath().startsWith("/api/quiz/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        ConsumptionProbe probe = rateLimitService.tryConsume(ip);

        if (!probe.isConsumed()) {
            long retryAfterSeconds = Math.max(
                    1,
                    (long) Math.ceil(
                            probe.getNanosToWaitForRefill() / 1000000000.0));

            response.setHeader("Retry-After", Long.toString(retryAfterSeconds));
            writeError(response, HttpStatus.TOO_MANY_REQUESTS,
                    "Too many requests. Try again later.");
            return;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null
                || !authorization.startsWith("Bearer ")
                || authorization.substring(7).isBlank()) {
            writeError(response, HttpStatus.UNAUTHORIZED,
                    "A valid bearer token is required");
            return;
        }

        try {
            String token = authorization.substring(7).trim();
            Long verifiedUserId = tokenVerifier.verifyAndGetUserId(token);

            // Yeah I'm still going to use the old fashioned way
            String suppliedUserId = request.getHeader("X-User-Id");
            if (suppliedUserId != null) {
                long parsedUserId;
                try {
                    parsedUserId = Long.parseLong(suppliedUserId);
                } catch (NumberFormatException exception) {
                    writeError(response, HttpStatus.FORBIDDEN,
                            "X-User-Id does not match the authenticated user");
                    return;
                }

                if (parsedUserId != verifiedUserId) {
                    writeError(response, HttpStatus.FORBIDDEN,
                            "X-User-Id does not match the authenticated user");
                    return;
                }
            }

            var authentication =
                    UsernamePasswordAuthenticationToken.authenticated(
                            verifiedUserId, null, List.of());

            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);
        } catch (BadCredentialsException exception) {
            SecurityContextHolder.clearContext();
            writeError(response, HttpStatus.UNAUTHORIZED,
                    "The access token is invalid or expired");
        } catch (AuthenticationServiceException exception) {
            SecurityContextHolder.clearContext();
            writeError(response, HttpStatus.SERVICE_UNAVAILABLE,
                    "The authentication service is temporarily unavailable");
        }
    }

    private static void writeError(
            HttpServletResponse response,
            HttpStatus status,
            String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}