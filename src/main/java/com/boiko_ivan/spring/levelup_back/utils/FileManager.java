package com.boiko_ivan.spring.levelup_back.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FileManager {



    public String generateKey(String name) {
        return UUID.fromString(name).toString();
    }
}
