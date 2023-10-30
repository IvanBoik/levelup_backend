package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;

    @Test
    public void findCourseByIDTest() {
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> courseService.findCourseByID(0)
        );
    }
}
