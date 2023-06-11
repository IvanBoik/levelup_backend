package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.entity.User;
import com.boiko_ivan.spring.levelup_back.repositories.UserRepository;
import com.boiko_ivan.spring.levelup_back.utils.PasswordMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordMailSender passwordMailSender;

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
    public void registration(User user) {
        boolean isDuplicate = userRepository.findByEmail(user.getEmail()).isPresent();
        if (isDuplicate) throw new RuntimeException("User with this email is already exists");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User googleAuthorization(User user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isPresent()) {
            return userFromDB.get();
        }
        else {
            String password = UUID.randomUUID().toString();
            user.setPassword(passwordEncoder.encode(password));
            passwordMailSender.sendGeneratedPassword(user.getEmail(), password);
            userRepository.save(user);
            return user;
        }
    }
}
