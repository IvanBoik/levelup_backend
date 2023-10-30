package com.boiko_ivan.spring.levelup_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class LevelupBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(LevelupBackApplication.class, args);
    }

}
