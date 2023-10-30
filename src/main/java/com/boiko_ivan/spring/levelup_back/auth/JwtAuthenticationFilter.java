package com.boiko_ivan.spring.levelup_back.auth;

import com.boiko_ivan.spring.levelup_back.entity.User;
import com.boiko_ivan.spring.levelup_back.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String requestHeader = request.getHeader("Authorization");
            String subject = null;
            String token;

            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                token = requestHeader.substring(7); // "Bearer ".length() == 7
                if (jwtHelper.validateAccessToken(token)) {
                    subject = jwtHelper.getSubjectFromAccessToken(token);
                }
            }

            if (subject != null) {
                Optional<User> optionalUser = userRepository.findByEmail(subject);
                if (optionalUser.isPresent()) {
                    UserDetails userDetails = optionalUser.get();
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                    );
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}