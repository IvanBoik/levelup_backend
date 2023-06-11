package com.boiko_ivan.spring.levelup_back.controllers;

import com.boiko_ivan.spring.levelup_back.entity.User;
import com.boiko_ivan.spring.levelup_back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/auth/registration")
    public ResponseEntity<Boolean> registration(@RequestBody User user) {
        try {
            userService.registration(user);
            return ResponseEntity.ok(true);
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/auth/google")
    public ResponseEntity<User> googleAuthorization(@RequestBody User user) {
        return ResponseEntity.ok(userService.googleAuthorization(user));
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

    @GetMapping("/test")
    public ResponseEntity<String> authorisationTest() {
        return ResponseEntity.ok("OK");
    }
}