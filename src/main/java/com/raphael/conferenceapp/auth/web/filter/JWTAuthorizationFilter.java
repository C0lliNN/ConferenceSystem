package com.raphael.conferenceapp.auth.web.filter;

import com.raphael.conferenceapp.auth.domain.User;
import com.raphael.conferenceapp.auth.exception.InvalidTokenException;
import com.raphael.conferenceapp.auth.web.TokenExtractor;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final TokenExtractor tokenExtractor;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/auth");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request);
            User user = tokenExtractor.extractUserFromToken(token);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);

            chain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw e;
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER)).
                filter(header -> header.startsWith(PREFIX)).
                map(header -> header.replace(PREFIX, ""))
                .orElseThrow(() -> new InvalidTokenException("The 'Authorization' header must be in the following format: 'Bearer token'"));
    }
}
