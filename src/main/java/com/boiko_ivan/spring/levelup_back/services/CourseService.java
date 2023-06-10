package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    Course getCourseByID(int id);
    void saveCourse(Course course);
    void deleteCourse(int id);
    List<Course> top5ByCompletions();
    List<Course> getAllCoursesWithSort(Sort sort);
    Page<Course> getPageOfCourses(Pageable pageable);
    List<Course> getAllCoursesWithSortByCompletions();
}
