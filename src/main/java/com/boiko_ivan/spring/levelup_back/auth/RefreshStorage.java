package com.boiko_ivan.spring.levelup_back.auth;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
@RedisHash("RefreshStorage")
public class RefreshStorage implements Serializable {
    @Id
    private String email;
    private String refreshToken;
}
