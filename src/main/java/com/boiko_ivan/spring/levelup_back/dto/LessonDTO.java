package com.boiko_ivan.spring.levelup_back.dto;

import com.boiko_ivan.spring.levelup_back.entity.HomeWork;

public record LessonDTO(
        long id,
        String title,
        long duration,
        String introduction,
        String description,
        String videoURL,
        HomeWork homeWork
) {
}
