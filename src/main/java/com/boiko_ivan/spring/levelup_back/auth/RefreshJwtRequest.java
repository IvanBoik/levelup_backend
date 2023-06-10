package com.boiko_ivan.spring.levelup_back.auth;

import lombok.*;

@Data
@Getter
@Setter
public class RefreshJwtRequest {
    private String refreshToken;
}
