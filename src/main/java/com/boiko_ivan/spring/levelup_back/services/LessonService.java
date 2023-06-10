package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.entity.Lesson;

import java.util.List;

public interface LessonService {
    List<Lesson> getAllLessons();
    Lesson getLessonByID(int id);
    void saveLesson(Lesson lesson);
    void deleteLesson(int id);
}
