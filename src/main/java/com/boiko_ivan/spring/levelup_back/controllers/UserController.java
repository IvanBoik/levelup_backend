package com.boiko_ivan.spring.levelup_back.controllers;

import com.boiko_ivan.spring.levelup_back.annotations.ValidateMultipartFile;
import com.boiko_ivan.spring.levelup_back.auth.*;
import com.boiko_ivan.spring.levelup_back.dto.UpdateUserDataRequest;
import com.boiko_ivan.spring.levelup_back.dto.UpdateUserPasswordRequest;
import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import com.boiko_ivan.spring.levelup_back.exceptions.EntityNotFoundException;
import com.boiko_ivan.spring.levelup_back.exceptions.InvalidJwtException;
import com.boiko_ivan.spring.levelup_back.exceptions.RefreshTokenExpiredException;
import com.boiko_ivan.spring.levelup_back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDTO getByID(@PathVariable long id) {
        return userService.findUserByID(id);
    }

    @PostMapping("/auth/sign_up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        JwtResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/sign_in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            JwtResponse response = userService.signIn(request);
            return ResponseEntity.ok(response);
        }
        catch (BadCredentialsException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @ValidateMultipartFile(ValidateMultipartFile.ValidFileTypes.IMAGE)
    @PostMapping(value = "/update/data/with_file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateUserData(
            @RequestPart("request")UpdateUserDataRequest request,
            @RequestPart("file") MultipartFile avatar
    ) {
        try {
            UserDTO userDTO = userService.updateUserData(request, avatar.getBytes());
            return ResponseEntity.ok(userDTO);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server can't read bytes from the file");
        }
    }

    @PostMapping("/update/data")
    public ResponseEntity<?> updateUserData(@RequestBody UpdateUserDataRequest request) {
        UserDTO userDTO = userService.updateUserData(request, null);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/update/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody UpdateUserPasswordRequest request) {
        userService.updateUserPassword(request);
        return ResponseEntity.ok("Password is updated");
    }

    @PostMapping("/auth/google")
    public ResponseEntity<GoogleAuthResponse> googleAuthorization(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.googleAuthorization(user));
    }

    @PostMapping("/auth/new_access_token")
    public ResponseEntity<?> getNewAccessToken(@RequestBody String refreshToken) {
        try {
            String accessToken = userService.getNewAccessToken(refreshToken);
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

    @GetMapping("/user_by_email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }
}