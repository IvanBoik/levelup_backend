package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.dto.CourseDTO;
import com.boiko_ivan.spring.levelup_back.dto.LessonDTO;
import com.boiko_ivan.spring.levelup_back.dto.LessonPreview;
import com.boiko_ivan.spring.levelup_back.dto.LessonsList;
import com.boiko_ivan.spring.levelup_back.entity.Course;
import com.boiko_ivan.spring.levelup_back.entity.FileInfo;
import com.boiko_ivan.spring.levelup_back.entity.Lesson;
import com.boiko_ivan.spring.levelup_back.exceptions.EntityNotFoundException;
import com.boiko_ivan.spring.levelup_back.exceptions.InvalidMultipartFileException;
import com.boiko_ivan.spring.levelup_back.mappers.CourseDTOMapper;
import com.boiko_ivan.spring.levelup_back.mappers.LessonDTOMapper;
import com.boiko_ivan.spring.levelup_back.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final FileService fileService;
    private final CourseDTOMapper courseDTOMapper;
    private final LessonDTOMapper lessonDTOMapper;
    
    public CourseDTO findCourseByID(long id) {
        return courseDTOMapper.apply(
                courseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course with id = %d is not found".formatted(id))
                )
        );
    }
    
    @Transactional(rollbackOn = {RuntimeException.class, EntityNotFoundException.class})
    public void saveCourseDraft(CourseDTO course, byte[] picture) {
        Course courseFromDB = courseRepository
                .findById(course.id())
                .orElseThrow(() ->
                        new EntityNotFoundException("Course with id = %d is not found".formatted(course.id()))
                );

        FileInfo fileInfo = courseFromDB.getPicture();
        if (fileInfo != null && fileInfo.getKey() != null) {
            fileService.updateFile(picture, fileInfo);
        }
        else {
            FileInfo newFileInfo = fileService.saveFile(picture);
            courseFromDB.setPicture(newFileInfo);
        }

        courseFromDB.setDescription(course.description());
        courseFromDB.setTitle(course.title());
        courseFromDB.setDifficulty(course.difficulty());
        courseFromDB.setTopic(course.topic());
        courseFromDB.setPrice(course.price());
        courseRepository.save(courseFromDB);
    }

    public void saveCourseDraft(CourseDTO course) {
        Course courseFromDB = courseRepository
                .findById(course.id())
                .orElseThrow(() ->
                        new EntityNotFoundException("Course with id = %d is not found".formatted(course.id()))
                );
        courseFromDB.setReady(false);
        //TODO обновлять информацию о курсе
        courseRepository.save(courseFromDB);
    }

    @Transactional(rollbackOn = {RuntimeException.class, EntityNotFoundException.class, InvalidMultipartFileException.class})
    public void saveLessonDraft(LessonDTO lesson, byte[] file) {
        Lesson lessonFromDB = lessonRepository
                .findById(lesson.id())
                .orElseThrow(() ->
                        new EntityNotFoundException("Lesson with id = %d is not found".formatted(lesson.id()))
                );
        FileInfo fileInfo = lessonFromDB.getVideo();
        if (fileInfo != null && fileInfo.getKey() != null) {
            fileService.updateFile(file, fileInfo);
        }
        else {
            FileInfo newFileInfo = fileService.saveFile(file);
            lessonFromDB.setVideo(newFileInfo);
        }

        lessonFromDB.setTitle(lesson.title());
        lessonFromDB.setIntroduction(lesson.introduction());
        lessonFromDB.setDescription(lesson.description());
        lessonFromDB.setDuration(lesson.duration());
        lessonFromDB.setHomeWork(lesson.homeWork());
        lessonRepository.save(lessonFromDB);
    }

    public void saveLessonDraft(LessonDTO lesson) {
        Lesson lessonFromDB = lessonRepository
                .findById(lesson.id())
                .orElseThrow(() ->
                        new EntityNotFoundException("Lesson with id = %d is not found".formatted(lesson.id()))
                );
        lessonFromDB.setDescription(lesson.description());
        lessonFromDB.setIntroduction(lesson.introduction());
        lessonFromDB.setTitle(lesson.title());
        lessonFromDB.setHomeWork(lesson.homeWork());
        lessonFromDB.setDuration(lesson.duration());
        lessonRepository.save(lessonFromDB);
    }
    
    public CourseDTO getCourseByAuthorID(long id) {
        return courseDTOMapper.apply(
                courseRepository.findDraftByAuthorID(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Course with id = %d is not found".formatted(id))
                        )
        );
    }
    
    public void publicationCourse(CourseDTO course) {
        Course courseFromDB = courseRepository
                .findById(course.id())
                .orElseThrow(() ->
                        new EntityNotFoundException("Course with id = %d is not found".formatted(course.id()))
                );
        courseFromDB.setReady(true);
        courseRepository.save(courseFromDB);
    }

    public List<CourseDTO> top5ByCompletions() {
        return courseRepository
                .top5ByCompletions()
                .stream()
                .map(courseDTOMapper)
                .toList();
    }

    public Page<CourseDTO> getPageOfCourses(Pageable pageable) {
        return new PageImpl<>(
                courseRepository
                .findAll(pageable)
                .stream()
                .map(courseDTOMapper)
                .toList()
        );
    }
    
    public LessonsList getLessonsByCourseID(long id, boolean isForDraft) {
        List<Lesson> lessonsFromDB = lessonRepository.getAllByCourseID(id);
        LessonDTO firstLesson = null;
        if (!isForDraft) {
            firstLesson = lessonDTOMapper.apply(lessonsFromDB.get(0));
        }

        return new LessonsList(
                firstLesson,
                lessonsFromDB
                        .stream()
                        .map(x -> new LessonPreview(x.getId(), x.getTitle(), x.getIntroduction()))
                        .toList()
        );
    }

    public LessonDTO getLessonByID(long id) {
        return lessonDTOMapper.apply(
                lessonRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Lesson with id = %d is not found".formatted(id))
                        )
        );
    }

    public void saveFutureWork(long id, byte[] file) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course with id = %d is not found".formatted(id))
                );

        FileInfo fileInfo = fileService.saveFile(file);
        course.getFutureWorks().add(fileInfo);
        courseRepository.save(course);
    }

    public void deleteFutureWork(long id) {
        fileService.deleteFile(id);
    }
}
