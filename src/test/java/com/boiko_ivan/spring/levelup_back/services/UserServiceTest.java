package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.dto.UpdateUserDataRequest;
import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import com.boiko_ivan.spring.levelup_back.entity.FileInfo;
import com.boiko_ivan.spring.levelup_back.entity.User;
import com.boiko_ivan.spring.levelup_back.exceptions.EntityNotFoundException;
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
    private String testPassword;
    @Value("${test.email}")
    private String testEmail;
    @Value("${test.id}")
    private long testID;

    @Test
    public void findUserDTOByIDTest() {
        UserDTO user = userService.findUserDTOByID(testID);
        Assertions.assertNotNull(user);
    }

    @Test
    public void findUserDTOByEmailTest() {
        UserDTO user = userService.findUserDTOByEmail(testEmail);
        Assertions.assertNotNull(user);
    }

    @Test
    public void findUserByIDTest() {
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.findUserByID(Long.MAX_VALUE)
        );
    }

    @Test
    public void findUserByEmailTest() {
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.findUserByEmail("findUserByEmailTest@test.test")
        );
    }

    @Test
    public void createAndSaveUserTest() {
        User createdUser = userService.createAndSaveUser(
                "test", "test", "test", "test"
        );
        User foundedUser = userRepository.findById(createdUser.getId()).orElse(null);

        Assertions.assertEquals(createdUser, foundedUser);

        userRepository.delete(createdUser);
    }

    @Test
    public void updateUserDataTest() {
        User userBeforeUpdate = User.builder()
                .name("test1")
                .surname("test1")
                .email("test1")
                .password("test1")
                .avatarFile(FileInfo.initWithKey(userService.getDefaultAvatarKey()))
                .build();
        userRepository.save(userBeforeUpdate);

        userService.updateUserData(
                new UpdateUserDataRequest(
                        userBeforeUpdate.getId(), "test2", "test2", "test2"
                )
        );
        User userAfterUpdate = userRepository.findById(userBeforeUpdate.getId()).orElseThrow();

        Assertions.assertNotEquals(userBeforeUpdate, userAfterUpdate);

        userRepository.delete(userAfterUpdate);
    }
}
