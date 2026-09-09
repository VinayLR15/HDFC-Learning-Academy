package com.hdfc.controller;

import com.hdfc.dto.CourseResponseDto;
import com.hdfc.service.CourseService;
import com.hdfc.service.EnrollmentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public AnalyticsController(
            CourseService courseService,
            EnrollmentService enrollmentService) {

        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/course-count")
    public ResponseEntity<Long> getCourseCount() {

        long count =
                courseService.getAllCourses()
                        .stream()
                        .count();

        return ResponseEntity.ok(count);
    }

    @GetMapping("/enrollment-count")
    public ResponseEntity<Long> getEnrollmentCount() {

        return ResponseEntity.ok(
                enrollmentService.getEnrollmentCount()
        );
    }

    @GetMapping("/most-popular-course")
    public ResponseEntity<CourseResponseDto>
            getMostPopularCourse() {

        return ResponseEntity.ok(
                courseService.getMostPopularCourse()
        );
    }
}