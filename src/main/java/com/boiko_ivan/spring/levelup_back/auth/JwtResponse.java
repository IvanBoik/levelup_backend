package com.boiko_ivan.spring.levelup_back.auth;

import com.boiko_ivan.spring.levelup_back.dto.UserDTO;
import com.boiko_ivan.spring.levelup_back.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
}
