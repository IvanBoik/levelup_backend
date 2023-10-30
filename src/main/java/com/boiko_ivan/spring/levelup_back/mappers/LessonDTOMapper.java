package com.boiko_ivan.spring.levelup_back.mappers;

import com.boiko_ivan.spring.levelup_back.dto.LessonDTO;
import com.boiko_ivan.spring.levelup_back.entity.Lesson;
import com.boiko_ivan.spring.levelup_back.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class LessonDTOMapper implements Function<Lesson, LessonDTO> {
    private final FileService fIleService;

    @Override
    public LessonDTO apply(Lesson lesson) {
        String url = fIleService.getURL(lesson.getVideo());
        return new LessonDTO(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDuration(),
                lesson.getIntroduction(),
                lesson.getDescription(),
                url,
                lesson.getHomeWork()
        );
    }
}
