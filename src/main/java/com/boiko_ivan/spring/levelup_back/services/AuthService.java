package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.auth.*;
import com.boiko_ivan.spring.levelup_back.dto.UpdateUserPasswordRequest;
import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import com.boiko_ivan.spring.levelup_back.entity.FileInfo;
import com.boiko_ivan.spring.levelup_back.entity.RefreshTokenEntity;
import com.boiko_ivan.spring.levelup_back.entity.User;
import com.boiko_ivan.spring.levelup_back.exceptions.*;
import com.boiko_ivan.spring.levelup_back.mappers.UserDTOMapper;
import com.boiko_ivan.spring.levelup_back.repositories.RefreshTokenRepository;
import com.boiko_ivan.spring.levelup_back.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final RefreshTokenRepository tokenRepository;
    private final JwtHelper jwtHelper;
    private final UserDTOMapper userDTOMapper;
    private final MailSenderService mailSenderService;

    public JwtResponse signIn(SignInRequest request) {
        authenticateAndSetToContext(request.email(), request.password());

        User user = userService.findUserByEmail(request.email());
        return JwtResponse.builder()
                .accessToken(jwtHelper.generateAccessToken(user))
                .refreshToken(getRefreshToken(user))
                .user(userDTOMapper.apply(user))
                .build();
    }

    public JwtResponse signUp(SignUpRequest request) {
        if (userRepository.existsUserByEmail(request.email())) {
            throw new BadCredentialsException("User with email %s already exists".formatted(request.email()));
        }
        setNewAuthenticationToContext(request.email(), request.password());

        User user = userService.createAndSaveUser(request);
        String refreshToken = createAndSaveRefreshToken(user);

        return JwtResponse.builder()
                .accessToken(jwtHelper.generateAccessToken(user))
                .refreshToken(refreshToken)
                .user(userDTOMapper.apply(user))
                .build();
    }

    public GoogleAuthResponse googleAuthorization(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userDTO.email());
        if (optionalUser.isPresent()) {
            return buildGoogleResponse(optionalUser.get(), false);
        }
        else {
            String password = UUID.randomUUID().toString();
            mailSenderService.sendGeneratedPassword(userDTO.email(), password);

            FileInfo fileInfo = buildFileInfo(userDTO);

            User userEntity = userService.createAndSaveUser(userDTO, password, fileInfo);
            return buildGoogleResponse(userEntity, true);
        }
    }

    private GoogleAuthResponse buildGoogleResponse(User user, boolean isNewUser) {
        return GoogleAuthResponse.builder()
                .user(userDTOMapper.apply(user))
                .isNewUser(isNewUser)
                .accessToken(jwtHelper.generateAccessToken(user))
                .refreshToken(getRefreshToken(user))
                .build();
    }

    private FileInfo buildFileInfo(UserDTO userDTO) {
        FileInfo fileInfo;
        if (userDTO.avatarURL() != null) {
            fileInfo = FileInfo.initWithPermanentURL(userDTO.avatarURL());
        }
        else {
            fileInfo = FileInfo.initWithKey(userService.getDefaultAvatarKey());
        }
        return fileInfo;
    }

    public void updateUserPassword(UpdateUserPasswordRequest request) {
        User userFromDB = userService.findUserByEmail(request.email());
        try {
            authenticateAndSetToContext(request.email(), request.oldPassword());
            userFromDB.setPassword(encoder.encode(request.newPassword()));
        }
        catch (AuthenticationException e) {
            throw new BadCredentialsException("Password %s is invalid".formatted(request.oldPassword()));
        }
        userRepository.save(userFromDB);
    }

    public String getNewAccessToken(String refreshToken) {
        if (!jwtHelper.validateRefreshToken(refreshToken)) {
            throw new InvalidJwtException("refresh token is not valid");
        }
        String email = jwtHelper.getSubjectFromRefreshToken(refreshToken);
        User user = userService.findUserByEmail(email);

        Optional<RefreshTokenEntity> optionalRefreshToken = tokenRepository.findById(email);
        if (optionalRefreshToken.isPresent()) {
            RefreshTokenEntity tokenEntity = optionalRefreshToken.get();
            if (jwtHelper.refreshIsExpired(tokenEntity.getToken())) {
                tokenRepository.deleteById(email);
                throw new RefreshTokenExpiredException("Please login");
            }
        }

        return jwtHelper.generateAccessToken(user);
    }

    private String getRefreshToken(UserDetails user) {
        Optional<RefreshTokenEntity> optionalRefreshToken = tokenRepository.findById(user.getUsername());
        if (optionalRefreshToken.isPresent()) {
            RefreshTokenEntity tokenEntity = optionalRefreshToken.get();
            if (jwtHelper.refreshIsExpired(tokenEntity.getToken())) {
                tokenRepository.deleteById(user.getUsername());
            }
            else {
                return tokenEntity.getToken();
            }
        }

        return createAndSaveRefreshToken(user);
    }

    private String createAndSaveRefreshToken(UserDetails user) {
        String token = jwtHelper.generateRefreshToken(user);
        tokenRepository.save(new RefreshTokenEntity(user.getUsername(), token));
        return token;
    }

    private void authenticateAndSetToContext(String email, String password) {
        SecurityContextHolder.getContext().setAuthentication(
                authenticate(email, password)
        );
    }

    private Authentication authenticate(String email, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }

    private void setNewAuthenticationToContext(String email, String password) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }
}
