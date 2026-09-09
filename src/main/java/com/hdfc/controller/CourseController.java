package com.hdfc.controller;

import com.hdfc.dto.CourseRequestDto;
import com.hdfc.dto.CourseResponseDto;
import com.hdfc.service.CourseService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(
            CourseService courseService) {

        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(
            @Valid @RequestBody CourseRequestDto request) {

        CourseResponseDto response =
                courseService.createCourse(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(
            @PathVariable("id") Integer id) {

        return ResponseEntity.ok(
                courseService.getCourseById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>>
            getAllCourses() {

        return ResponseEntity.ok(
                courseService.getAllCourses()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable("id") Integer id,
            @Valid @RequestBody CourseRequestDto request) {

        return ResponseEntity.ok(
                courseService.updateCourse(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable("id") Integer id) {

        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trainer/{trainerName}")
    public ResponseEntity<List<CourseResponseDto>>
            getCoursesByTrainer(
                    @PathVariable String trainerName) {

        return ResponseEntity.ok(
                courseService.getCoursesByTrainer(
                        trainerName
                )
        );
    }

    @GetMapping("/fees/{amount}")
    public ResponseEntity<List<CourseResponseDto>>
            getCoursesByFeesLessThan(
                    @PathVariable Double amount) {

        return ResponseEntity.ok(
                courseService.getCoursesByFeesLessThan(
                        amount
                )
        );
    }
}