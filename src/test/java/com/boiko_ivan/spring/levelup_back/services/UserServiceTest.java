package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.auth.JwtResponse;
import com.boiko_ivan.spring.levelup_back.auth.SignInRequest;
import com.boiko_ivan.spring.levelup_back.auth.SignUpRequest;
import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import com.boiko_ivan.spring.levelup_back.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Value("${test.password}")
    private String password;
    @Value("${test.email}")
    private String email;

    @Test
    public void findUserByIDTest() {
        UserDTO user = userService.findUserByID(1);
        Assertions.assertNotNull(user);
    }

    @Test
    public void findUserByEmailTest() {
        UserDTO user = userService.findUserByEmail(email);
        Assertions.assertNotNull(user);
    }

    @Test
    public void signInTest() {
        JwtResponse response = userService.signIn(
                new SignInRequest(email, password)
        );

        Assertions.assertNotNull(response);
    }

    @Test
    public void signUpTest() {
        JwtResponse response = userService.signUp(
                new SignUpRequest("Test", "Test", "Test", "Test")
        );
        Assertions.assertNotNull(response);
        userRepository.deleteById(response.getUser().id());
    }
}
