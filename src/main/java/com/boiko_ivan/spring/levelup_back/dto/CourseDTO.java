package com.boiko_ivan.spring.levelup_back.dto;

import java.time.LocalDateTime;

public record CourseDTO(
        long id,
        long authorID,
        String authorName,
        String authorSurname,
        String title,
        String description,
        double price,
        LocalDateTime dateOfCreate,
        String pictureURL,
        String difficulty,
        String topic
) {
}
