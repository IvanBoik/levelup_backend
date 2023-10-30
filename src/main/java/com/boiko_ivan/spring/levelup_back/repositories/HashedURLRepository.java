package com.boiko_ivan.spring.levelup_back.repositories;

import com.boiko_ivan.spring.levelup_back.entity.HashedURL;
import org.springframework.data.repository.CrudRepository;

public interface HashedURLRepository extends CrudRepository<HashedURL, String> {
}
