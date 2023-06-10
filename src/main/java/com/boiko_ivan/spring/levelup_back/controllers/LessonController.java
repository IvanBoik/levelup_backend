package com.boiko_ivan.spring.levelup_back.controllers;

import com.boiko_ivan.spring.levelup_back.entity.Lesson;
import com.boiko_ivan.spring.levelup_back.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @GetMapping("/")
    public List<Lesson> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @PostMapping("/")
    public void saveLesson(@RequestBody Lesson lesson) {
        lessonService.saveLesson(lesson);
    }

    @PutMapping("/")
    public void updateLesson(@RequestBody Lesson lesson) {
        lessonService.saveLesson(lesson);
    }

    @GetMapping("/{id}")
    public Lesson getLessonByID(@PathVariable int id) {
        return lessonService.getLessonByID(id);
    }

    @DeleteMapping("/{id}")
    public void deleteLessonByID(@PathVariable int id) {
        lessonService.deleteLesson(id);
    }
}
