package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.auth.AuthenticationRequest;
import com.boiko_ivan.spring.levelup_back.auth.AuthenticationResponse;
import com.boiko_ivan.spring.levelup_back.auth.RefreshStorage;
import com.boiko_ivan.spring.levelup_back.config.JwtService;
import com.boiko_ivan.spring.levelup_back.entity.Role;
import com.boiko_ivan.spring.levelup_back.entity.User;
import com.boiko_ivan.spring.levelup_back.repositories.RefreshStorageRepository;
import com.boiko_ivan.spring.levelup_back.repositories.UserRepository;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final RefreshStorageRepository refreshStorageRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByID(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AuthenticationResponse register(User user) {
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);
        refreshStorageRepository.save(RefreshStorage
                .builder()
                .email(user.getEmail())
                .refreshToken(refresh)
                .build());
        return AuthenticationResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .user(user)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        System.out.println(user);
        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);
        refreshStorageRepository.save(RefreshStorage
                .builder()
                .email(user.getEmail())
                .refreshToken(refresh)
                .build());
        return AuthenticationResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .user(user)
                .build();
    }

    public AuthenticationResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtService.validateRefreshToken(refreshToken)) {
            Claims claims = jwtService.getRefreshClaims(refreshToken);
            String email = claims.getSubject();
            String saveRefreshToken = refreshStorageRepository
                    .findById(email)
                    .orElseThrow()
                    .getRefreshToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow();
                String accessToken = jwtService.generateAccessToken(user);
                return AuthenticationResponse
                        .builder()
                        .accessToken(accessToken)
                        .refreshToken(null)
                        .user(user)
                        .build();
                }
            }
        return AuthenticationResponse
                .builder()
                .accessToken(null)
                .refreshToken(null)
                .user(null)
                .build();
    }

    public AuthenticationResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtService.validateRefreshToken(refreshToken)) {
            Claims claims = jwtService.getRefreshClaims(refreshToken);
            String email = claims.getSubject();
            String saveRefreshToken = refreshStorageRepository
                    .findById(email)
                    .orElseThrow()
                    .getRefreshToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow();
                String accessToken = jwtService.generateAccessToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);
                refreshStorageRepository.save(RefreshStorage
                        .builder()
                        .email(email)
                        .refreshToken(newRefreshToken)
                        .build());
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .user(user)
                        .build();
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }
}
