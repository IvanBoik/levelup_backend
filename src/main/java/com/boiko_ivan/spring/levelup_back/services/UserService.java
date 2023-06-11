package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User getUserByID(int id);
    void saveUser(User user);
    void deleteUser(int id);

    Optional<User> findByEmail(String email);
    void registration(User user);
    User googleAuthorization(User user);
}
