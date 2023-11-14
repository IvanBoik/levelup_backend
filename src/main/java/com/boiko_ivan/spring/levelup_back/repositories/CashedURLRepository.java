package com.boiko_ivan.spring.levelup_back.repositories;

import com.boiko_ivan.spring.levelup_back.entity.CashedURL;
import org.springframework.data.repository.CrudRepository;

public interface CashedURLRepository extends CrudRepository<CashedURL, String> {
}
