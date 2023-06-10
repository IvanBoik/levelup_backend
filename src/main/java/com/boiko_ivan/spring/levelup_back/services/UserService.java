package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.auth.AuthenticationRequest;
import com.boiko_ivan.spring.levelup_back.auth.AuthenticationResponse;
import com.boiko_ivan.spring.levelup_back.entity.User;
import jakarta.security.auth.message.AuthException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User getUserByID(int id);
    void saveUser(User user);
    void deleteUser(int id);

    Optional<User> findByEmail(String email);
    AuthenticationResponse register(User request);
    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse getAccessToken(String refreshToken);
    AuthenticationResponse refresh(String refreshToken) throws AuthException;
}
