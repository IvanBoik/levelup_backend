package com.boiko_ivan.spring.levelup_back.auth;

import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAuthResponse {
    private UserDTO user;
    private String accessToken;
    private String refreshToken;
    private boolean isNewUser;
}
