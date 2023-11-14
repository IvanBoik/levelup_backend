package com.boiko_ivan.spring.levelup_back.controllers;

import com.boiko_ivan.spring.levelup_back.auth.GoogleAuthResponse;
import com.boiko_ivan.spring.levelup_back.auth.JwtResponse;
import com.boiko_ivan.spring.levelup_back.auth.SignInRequest;
import com.boiko_ivan.spring.levelup_back.auth.SignUpRequest;
import com.boiko_ivan.spring.levelup_back.dto.UpdateUserPasswordRequest;
import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import com.boiko_ivan.spring.levelup_back.exceptions.EntityNotFoundException;
import com.boiko_ivan.spring.levelup_back.exceptions.InvalidJwtException;
import com.boiko_ivan.spring.levelup_back.exceptions.RefreshTokenExpiredException;
import com.boiko_ivan.spring.levelup_back.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/sign_up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        JwtResponse response = authService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/sign_in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            JwtResponse response = authService.signIn(request);
            return ResponseEntity.ok(response);
        }
        catch (BadCredentialsException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/auth/google")
    public ResponseEntity<GoogleAuthResponse> googleAuthorization(@RequestBody UserDTO user) {
        return ResponseEntity.ok(authService.googleAuthorization(user));
    }

    @PostMapping("/update/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody UpdateUserPasswordRequest request) {
        authService.updateUserPassword(request);
        return ResponseEntity.ok("Password is updated");
    }

    @PostMapping("/auth/new_access_token")
    public ResponseEntity<?> getNewAccessToken(@RequestBody String refreshToken) {
        try {
            String accessToken = authService.getNewAccessToken(refreshToken);
            return ResponseEntity.ok(accessToken);
        }
        catch (InvalidJwtException | EntityNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
        catch (RefreshTokenExpiredException e) {
            return ResponseEntity
                    .status(HttpStatus.SEE_OTHER)
                    .body(e.getMessage());
        }
    }
}
