package com.boiko_ivan.spring.levelup_back.mappers;

import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import com.boiko_ivan.spring.levelup_back.entity.User;
import com.boiko_ivan.spring.levelup_back.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserDTOMapper implements Function<User, UserDTO> {
    private final FileService fIleService;

    @Override
    public UserDTO apply(User user) {
        String url = fIleService.getURL(user.getAvatarFile());
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getSurname(),
                url
        );
    }
}
