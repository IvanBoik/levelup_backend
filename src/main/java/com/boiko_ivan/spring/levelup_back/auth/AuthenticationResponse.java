package com.boiko_ivan.spring.levelup_back.auth;

import com.boiko_ivan.spring.levelup_back.entity.User;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private User user;
}
