package com.boiko_ivan.spring.levelup_back.controllers;

import com.boiko_ivan.spring.levelup_back.annotations.ValidateMultipartFile;
import com.boiko_ivan.spring.levelup_back.dto.UpdateUserDataRequest;
import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import com.boiko_ivan.spring.levelup_back.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("user_by_id/{id}")
    public UserDTO getUserByID(@PathVariable long id) {
        return userService.findUserDTOByID(id);
    }

    @GetMapping("/user_by_email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserDTOByEmail(email));
    }

    @ValidateMultipartFile(ValidateMultipartFile.ValidFileTypes.IMAGE)
    @PostMapping(value = "/update/data/with_file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateUserData(
            @RequestPart("userID") long userID,
            @RequestPart("file") MultipartFile avatar
    ) {
        try {
            UserDTO userDTO = userService.updateUserAvatar(userID, avatar.getBytes());
            return ResponseEntity.ok(userDTO);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server can't read bytes from the file");
        }
    }

    @PostMapping("/update/data")
    public ResponseEntity<?> updateUserData(@RequestBody UpdateUserDataRequest request) {
        UserDTO userDTO = userService.updateUserData(request);
        return ResponseEntity.ok(userDTO);
    }
}