package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.entity.Lesson;
import com.boiko_ivan.spring.levelup_back.repositories.LessonRepository;
import com.boiko_ivan.spring.levelup_back.utils.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private FileManager fileManager;

    @Override
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson getLessonByID(int id) {
        return lessonRepository.findById(id).orElse(null);
    }

    @Override
    public void saveLesson(Lesson lesson) {
//        MultipartFile resource = null;
//        Video video1 = new Video(resource.getOriginalFilename(), fileManager.generateKey(resource.getName()), );
        lessonRepository.save(lesson);
    }

    @Override
    public void deleteLesson(int id) {
        lessonRepository.deleteById(id);
    }
}
