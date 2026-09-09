package com.hdfc.repository;

import com.hdfc.entity.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CourseRepository {

    private final Map<Integer, Course> courseMap =
            new ConcurrentHashMap<>();

    public Course save(Course course) {

        courseMap.put(course.getCourseId(), course);

        return course;
    }

    public Course findById(Integer courseId) {

        return courseMap.get(courseId);
    }

    public List<Course> findAll() {

        return new ArrayList<>(courseMap.values());
    }

    public void deleteById(Integer courseId) {

        courseMap.remove(courseId);
    }

    public boolean existsById(Integer courseId) {

        return courseMap.containsKey(courseId);
    }
}