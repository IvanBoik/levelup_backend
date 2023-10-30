package com.boiko_ivan.spring.levelup_back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@RedisHash("RefreshToken")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashedURL {
    @Id
    private String key;
    private String url;
    private Date expiration;

    public HashedURL(String key) {
        this.key = key;
    }
}
