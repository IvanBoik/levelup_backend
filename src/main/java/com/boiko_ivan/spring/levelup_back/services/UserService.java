package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.auth.*;
import com.boiko_ivan.spring.levelup_back.dto.UpdateUserDataRequest;
import com.boiko_ivan.spring.levelup_back.dto.UpdateUserPasswordRequest;
import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import com.boiko_ivan.spring.levelup_back.entity.*;
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
public class UserService {
    private final UserRepository userRepository;
    private final FileService fIleService;
    private final MailSenderService mailSenderService;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository tokenRepository;
    private final UserDTOMapper userDTOMapper;


    public void updateUserPassword(UpdateUserPasswordRequest request) {
        User userFromDB = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new EntityNotFoundException("User with email = %s is not found".formatted(request.email()))
                );
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.oldPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            userFromDB.setPassword(encoder.encode(request.newPassword()));
        }
        catch (AuthenticationException e) {
            throw new InvalidPasswordException("Password %s is invalid".formatted(request.oldPassword()));
        }
        userRepository.save(userFromDB);
    }

    public UserDTO updateUserData(UpdateUserDataRequest request, byte[] avatar) {
        User userFromDB = userRepository.findById(request.id())
                .orElseThrow(() ->
                        new EntityNotFoundException("User with id = %d is not found".formatted(request.id()))
                );
        userFromDB.setSurname(request.newSurname());
        userFromDB.setName(request.newName());
        userFromDB.setEmail(request.newEmail());

        if (avatar != null) {
            FileInfo fileInfo = userFromDB.getAvatarFile();
            if (fileInfo != null && fileInfo.getKey() != null) {
                fIleService.updateFile(avatar, fileInfo);
            }
            else {
                FileInfo newFileInfo = fIleService.saveFile(avatar);
                userFromDB.setAvatarFile(newFileInfo);
            }
        }

        return userDTOMapper.apply(
                userRepository.save(userFromDB)
        );
    }

    public UserDTO findUserByID(long id) {
        return userDTOMapper.apply(
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("User with id = %d is not found".formatted(id))
                        )
        );
    }
    
    public UserDTO findUserByEmail(String email) {
        return userDTOMapper.apply(
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new EntityNotFoundException("User with email = %s is not found".formatted(email))
                        )
        );
    }

    public JwtResponse signIn(SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Incorrect login or password"));

        String refreshToken = getRefreshToken(user);

        return JwtResponse.builder()
                .accessToken(jwtHelper.generateAccessToken(user))
                .refreshToken(refreshToken)
                .user(userDTOMapper.apply(user))
                .build();
    }

    public JwtResponse signUp(SignUpRequest request) {
        boolean isDuplicate = userRepository.findByEmail(request.getEmail()).isPresent();
        if (isDuplicate) {
            throw new DuplicateEmailException("User with email %s already exists".formatted(request.getEmail()));
        }

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User newUser = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        User userFromDB = userRepository.save(newUser);
        String refreshToken = jwtHelper.generateRefreshToken(userFromDB);
        tokenRepository.save(new RefreshTokenEntity(userFromDB.getEmail(), refreshToken));

        return JwtResponse.builder()
                .accessToken(jwtHelper.generateAccessToken(userFromDB))
                .refreshToken(refreshToken)
                .user(userDTOMapper.apply(userFromDB))
                .build();
    }

    public GoogleAuthResponse googleAuthorization(UserDTO user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.email());
        if (userFromDB.isPresent()) {
            return GoogleAuthResponse.builder()
                    .user(userDTOMapper.apply(userFromDB.get()))
                    .isNewUser(false)
                    .accessToken(jwtHelper.generateAccessToken(userFromDB.get()))
                    .refreshToken(getRefreshToken(userFromDB.get()))
                    .build();
        }
        else {
            String password = UUID.randomUUID().toString();
            mailSenderService.sendGeneratedPassword(user.email(), password);

            FileInfo fileInfo = null;
            if (user.avatarURL() != null) {
                fileInfo = FileInfo.builder()
                        .permanentURL(user.avatarURL())
                        .build();
            }

            User userEntity = User.builder()
                    .name(user.name())
                    .surname(user.surname())
                    .email(user.email())
                    .password(encoder.encode(password))
                    .role(Role.USER)
                    .avatarFile(fileInfo)
                    .build();
            userRepository.save(userEntity);

            return GoogleAuthResponse.builder()
                    .user(userDTOMapper.apply(userEntity))
                    .isNewUser(true)
                    .accessToken(jwtHelper.generateAccessToken(userEntity))
                    .refreshToken(jwtHelper.generateRefreshToken(userEntity))
                    .build();
        }
    }

    public String getNewAccessToken(String refreshToken) {
        if (!jwtHelper.validateRefreshToken(refreshToken)) {
            throw new InvalidJwtException("refresh token is not valid");
        }
        String email = jwtHelper.getSubjectFromRefreshToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with email = %s is not found".formatted(email))
                );

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
            if (!jwtHelper.refreshIsExpired(tokenEntity.getToken())) {
                return tokenEntity.getToken();
            }
            else {
                tokenRepository.deleteById(user.getUsername());
            }
        }

        String newRefreshToken = jwtHelper.generateRefreshToken(user);
        tokenRepository.save(new RefreshTokenEntity(user.getUsername(), newRefreshToken));
        return newRefreshToken;
    }
}
