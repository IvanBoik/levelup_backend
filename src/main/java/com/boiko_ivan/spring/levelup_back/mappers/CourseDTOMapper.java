package com.boiko_ivan.spring.levelup_back.mappers;

import com.boiko_ivan.spring.levelup_back.dto.CourseDTO;
import com.boiko_ivan.spring.levelup_back.entity.Course;
import com.boiko_ivan.spring.levelup_back.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CourseDTOMapper implements Function<Course, CourseDTO > {
    private final FileService fIleService;

    @Override
    public CourseDTO apply(Course course) {
        String url = fIleService.getURL(course.getPicture());
        return new CourseDTO(
                course.getId(),
                course.getAuthor().getId(),
                course.getAuthor().getName(),
                course.getAuthor().getSurname(),
                course.getTitle(),
                course.getDescription(),
                course.getPrice(),
                course.getDateOfCreate(),
                url,
                course.getDifficulty(),
                course.getTopic()
        );
    }
}
