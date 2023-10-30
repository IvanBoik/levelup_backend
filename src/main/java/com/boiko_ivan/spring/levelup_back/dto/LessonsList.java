package com.boiko_ivan.spring.levelup_back.dto;

import java.util.List;

public record LessonsList(
        LessonDTO firstLesson,
        List<LessonPreview> previews
) {
}
