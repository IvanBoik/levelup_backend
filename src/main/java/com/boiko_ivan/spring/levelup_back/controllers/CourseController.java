package com.boiko_ivan.spring.levelup_back.controllers;

import com.amazonaws.SdkClientException;
import com.boiko_ivan.spring.levelup_back.annotations.ValidateMultipartFile;
import com.boiko_ivan.spring.levelup_back.dto.CourseDTO;
import com.boiko_ivan.spring.levelup_back.dto.LessonDTO;
import com.boiko_ivan.spring.levelup_back.dto.LessonsList;
import com.boiko_ivan.spring.levelup_back.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private static final int COURSE_PAGE_SIZE = 15;

    @ValidateMultipartFile(ValidateMultipartFile.ValidFileTypes.IMAGE)
    @PostMapping(value = "/course_draft/with_picture", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> saveCourseDraft(
            @RequestPart("course") CourseDTO course,
            @RequestPart("picture") MultipartFile picture
    ) {
        try {
            courseService.saveCourseDraft(course, picture.getBytes());
            return ResponseEntity.ok("Course updated");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server can't read bytes from the file");
        }
    }

    @PostMapping("/course_draft")
    public void saveCourseDraft(@RequestBody CourseDTO course) {
        courseService.saveCourseDraft(course);
    }

    @ValidateMultipartFile(ValidateMultipartFile.ValidFileTypes.VIDEO)
    @PostMapping(value = "/lesson_draft/with_picture", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> saveLessonDraft(
            @RequestPart("lesson") LessonDTO lesson,
            @RequestPart("file") MultipartFile file
    ) {
        try {
            courseService.saveLessonDraft(lesson, file.getBytes());
            return ResponseEntity.ok("Lesson updated");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server can't read bytes from the file");
        }
    }

    @ValidateMultipartFile(ValidateMultipartFile.ValidFileTypes.IMAGE)
    @PostMapping(value = "/save_future_work", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> saveFutureWork(
            @RequestPart("course_id") long id,
            @RequestPart("file") MultipartFile file
    ) {
        try {
            courseService.saveFutureWork(id, file.getBytes());
            return ResponseEntity.ok("File saved");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server can't read bytes from the file");
        }
    }

    @DeleteMapping("/delete_future_work/file_id={id}")
    public ResponseEntity<?> deleteFutureWork(@PathVariable long id) {
        try {
            courseService.deleteFutureWork(id);
            return ResponseEntity.ok("File deleted");
        }
        catch (SdkClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request for S3");
        }
    }

    @PostMapping("/lesson_draft")
    public void saveLessonDraft(@RequestBody LessonDTO lesson) {
        courseService.saveLessonDraft(lesson);
    }

    @GetMapping("/course_draft/{id}")
    public ResponseEntity<CourseDTO> getCourseDraft(@PathVariable long id) {
        return ResponseEntity.ok(courseService.getCourseByAuthorID(id));
    }

    @GetMapping("/lessons_by_course_id")
    public ResponseEntity<LessonsList> getLessonsByCourseID(
            @RequestParam long id,
            @RequestParam boolean isForDraft
    ) {
        return ResponseEntity.ok(courseService.getLessonsByCourseID(id, isForDraft));
    }

    @GetMapping("/lessons/{id}")
    public ResponseEntity<LessonDTO> getLessonByID(@PathVariable long id) {
        return ResponseEntity.ok(courseService.getLessonByID(id));
    }

    @PostMapping("/publication")
    public void publicationCourse(@RequestBody CourseDTO course) {
        courseService.publicationCourse(course);
    }

    @GetMapping("/auth/{id}")
    public ResponseEntity<CourseDTO> getCourseByID(@PathVariable long id) {
        return ResponseEntity.ok(courseService.findCourseByID(id));
    }

    @GetMapping("/auth/top5")
    public ResponseEntity<List<CourseDTO>> top5ByPopularity() {
        return ResponseEntity.ok(courseService.top5ByCompletions());
    }

    @GetMapping("/sorted_page")
    public ResponseEntity<Page<CourseDTO>> getPageOfSortedCourses(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "sort") String sortField
    ) {
        return ResponseEntity.ok(
                courseService.getPageOfCourses(
                        PageRequest.of(page, COURSE_PAGE_SIZE, Sort.by(sortField))
                )
        );
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CourseDTO>> getPageOfCourses(@RequestParam(value = "page") int page) {
        return ResponseEntity.ok(
                courseService.getPageOfCourses(
                        PageRequest.of(page, COURSE_PAGE_SIZE)
                )
        );
    }
}
