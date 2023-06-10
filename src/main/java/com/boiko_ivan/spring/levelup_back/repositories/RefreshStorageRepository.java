package com.boiko_ivan.spring.levelup_back.repositories;

import com.boiko_ivan.spring.levelup_back.auth.RefreshStorage;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface RefreshStorageRepository extends KeyValueRepository<RefreshStorage, String> {
}
