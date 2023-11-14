package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.auth.SignUpRequest;
import com.boiko_ivan.spring.levelup_back.dto.UpdateUserDataRequest;
import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import com.boiko_ivan.spring.levelup_back.entity.FileInfo;
import com.boiko_ivan.spring.levelup_back.entity.Role;
import com.boiko_ivan.spring.levelup_back.entity.User;
import com.boiko_ivan.spring.levelup_back.exceptions.EntityNotFoundException;
import com.boiko_ivan.spring.levelup_back.mappers.UserDTOMapper;
import com.boiko_ivan.spring.levelup_back.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FileService fIleService;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder encoder;

    @Value("${default.user-avatar}")
    private final String defaultAvatarKey;

    public UserDTO updateUserData(UpdateUserDataRequest request) {
        User userFromDB = findUserByID(request.id());
        userFromDB.setSurname(request.newSurname());
        userFromDB.setName(request.newName());
        userFromDB.setEmail(request.newEmail());

        return userDTOMapper.apply(
                userRepository.save(userFromDB)
        );
    }

    public UserDTO updateUserAvatar(long userID, byte[] avatar) {
        User user = findUserByID(userID);
        FileInfo fileInfo = user.getAvatarFile();
        if (fileInfo != null && fileInfo.getKey() != null) {
            fIleService.updateFile(avatar, fileInfo);
        }
        else {
            FileInfo newFileInfo = fIleService.saveFile(avatar);
            user.setAvatarFile(newFileInfo);
        }

        return userDTOMapper.apply(
                userRepository.save(user)
        );
    }

    public UserDTO findUserDTOByID(long id) {
        return userDTOMapper.apply(findUserByID(id));
    }
    
    public UserDTO findUserDTOByEmail(String email) {
        return userDTOMapper.apply(findUserByEmail(email));
    }

    public User findUserByID(long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with id = %d is not found".formatted(id))
                );
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with email = %s is not found".formatted(email))
                );
    }

    public User createAndSaveUser(UserDTO userDTO, String password, FileInfo fileInfo) {
        return createAndSaveUser(userDTO.name(), userDTO.surname(), userDTO.email(), password, fileInfo);
    }

    public User createAndSaveUser(SignUpRequest request) {
        return createAndSaveUser(request.name(), request.surname(), request.email(), request.password());
    }

    public User createAndSaveUser(String name, String surname, String email, String password) {
        return createAndSaveUser(name, surname, email, password, FileInfo.initWithKey(defaultAvatarKey));
    }

    public User createAndSaveUser(String name, String surname, String email, String password, FileInfo avatar) {
        User user = User.builder()
                .name(name)
                .surname(surname)
                .email(email)
                .password(encoder.encode(password))
                .role(Role.USER)
                .avatarFile(avatar)
                .build();
        userRepository.save(user);
        return user;
    }

    public String getDefaultAvatarKey() {
        return defaultAvatarKey;
    }
}
