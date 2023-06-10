package com.boiko_ivan.spring.levelup_back.repositories;

import com.boiko_ivan.spring.levelup_back.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query(nativeQuery = true, value = "select courses.* from courses left join completions c " +
                                        "on courses.id = c.id_course " +
                                        "group by courses.id " +
                                        "order by COUNT(c.id) DESC " +
                                        "limit 5")
    List<Course> top5ByCompletions();

    @Query(nativeQuery = true, value = "select courses.* from courses left join completions c " +
                                        "on courses.id = c.id_course " +
                                        "group by courses.id " +
                                        "order by COUNT(c.id) DESC ")
    List<Course> findAllByCompletions();
}
