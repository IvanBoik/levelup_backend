package com.boiko_ivan.spring.levelup_back.repositories;

import com.boiko_ivan.spring.levelup_back.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {
}
