package com.boiko_ivan.spring.levelup_back.controllers;

import com.boiko_ivan.spring.levelup_back.entity.Course;
import com.boiko_ivan.spring.levelup_back.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    private List<Course> courses;
    private String lastSortField;
    private static final int COURSE_PAGE_SIZE = 15;

    @GetMapping("/auth")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/")
    public void saveCourse(@RequestBody Course course) {
        courseService.saveCourse(course);
    }

    @PutMapping("/")
    public void updateCourse(@RequestBody Course course) {
        courseService.saveCourse(course);
    }

    @GetMapping("/auth/{id}")
    public Course getCourseByID(@PathVariable int id) {
        return courseService.getCourseByID(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCourseByID(@PathVariable int id) {
        courseService.deleteCourse(id);
    }

    @GetMapping("/auth/top5")
    public List<Course> top5ByPopularity() {
        return courseService.top5ByCompletions();
    }

    @GetMapping("/sorted_page")
    public List<Course> getPageOfSortedCourses(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "sort") String sortField) {

        if (!Objects.equals(lastSortField, sortField)) {
            if (Objects.equals(sortField, "completions")) {
                courses = courseService.getAllCoursesWithSortByCompletions();
            }
            else {
                courses = courseService.getAllCoursesWithSort(Sort.by(sortField));
                lastSortField = sortField;
            }
        }

        try {
            return courses.subList(page * COURSE_PAGE_SIZE, COURSE_PAGE_SIZE);
        }
        catch (IndexOutOfBoundsException e1) {
            try {
                return courses.subList(page * COURSE_PAGE_SIZE, courses.size() != 0 ? courses.size()-1 : 0);
            }
            catch (IndexOutOfBoundsException e2) {
                return courses;
            }
        }
    }

    @GetMapping("/page")
    public Page<Course> getPageOfCourses(@RequestParam(value = "page") int page) {
        return courseService.getPageOfCourses(PageRequest.of(page, COURSE_PAGE_SIZE));
    }
}
