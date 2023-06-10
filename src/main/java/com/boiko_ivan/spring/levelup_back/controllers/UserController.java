package com.boiko_ivan.spring.levelup_back.controllers;

import com.boiko_ivan.spring.levelup_back.auth.AuthenticationRequest;
import com.boiko_ivan.spring.levelup_back.auth.AuthenticationResponse;
import com.boiko_ivan.spring.levelup_back.auth.RefreshJwtRequest;
import com.boiko_ivan.spring.levelup_back.entity.User;
import com.boiko_ivan.spring.levelup_back.services.UserService;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/")
    public void updateUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public User getUserByID(@PathVariable int id) {
        return userService.getUserByID(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserByID(@PathVariable int id) {
        userService.deleteUser(id);
    }

    @GetMapping("/user_by_email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email).orElseThrow());
    }

    @PostMapping("/auth/new_access")
    public ResponseEntity<AuthenticationResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        AuthenticationResponse token = userService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/new_refresh")
    public ResponseEntity<AuthenticationResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        AuthenticationResponse token = userService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/test")
    public ResponseEntity<String> authorisationTest() {
        return ResponseEntity.ok("OK");
    }
}