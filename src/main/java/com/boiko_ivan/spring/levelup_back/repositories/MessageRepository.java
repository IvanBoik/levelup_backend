package com.boiko_ivan.spring.levelup_back.repositories;

import com.boiko_ivan.spring.levelup_back.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
