package com.hdfc.service;

import com.hdfc.dto.CourseRequestDto;
import com.hdfc.dto.CourseResponseDto;
import com.hdfc.entity.Course;
import com.hdfc.exception.CourseNotFoundException;
import com.hdfc.mapper.CourseMapper;
import com.hdfc.repository.CourseRepository;
import com.hdfc.repository.EnrollmentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMapper courseMapper;

    private final AtomicInteger courseIdGenerator =
            new AtomicInteger(0);

    public CourseServiceImpl(
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository,
            CourseMapper courseMapper) {

        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public CourseResponseDto createCourse(
            CourseRequestDto request) {

        Course course = courseMapper.toEntity(request);

        course.setCourseId(
                courseIdGenerator.incrementAndGet()
        );

        Course savedCourse =
                courseRepository.save(course);

        return courseMapper.toResponseDto(savedCourse);
    }

    @Override
    public CourseResponseDto getCourseById(
            Integer courseId) {

        Course course =
                courseRepository.findById(courseId);

        if (course == null) {
            throw new CourseNotFoundException(
                    "Course not found with ID: " + courseId
            );
        }

        return courseMapper.toResponseDto(course);
    }
    
    @Override
    public CourseResponseDto getMostPopularCourse() {

        List<Course> courses =
                courseRepository.findAll();

        if (courses.isEmpty()) {
            return null;
        }

        return courses.stream()
                .max((course1, course2) -> {

                    long count1 =
                            enrollmentRepository.findAll()
                                    .stream()
                                    .filter(enrollment ->
                                            enrollment.getCourseId()
                                                    .equals(course1.getCourseId()))
                                    .count();

                    long count2 =
                            enrollmentRepository.findAll()
                                    .stream()
                                    .filter(enrollment ->
                                            enrollment.getCourseId()
                                                    .equals(course2.getCourseId()))
                                    .count();

                    return Long.compare(count1, count2);

                })
                .map(courseMapper::toResponseDto)
                .orElse(null);
    }

    @Override
    public List<CourseResponseDto> getAllCourses() {

        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponseDto updateCourse(
            Integer courseId,
            CourseRequestDto request) {

        Course existingCourse =
                courseRepository.findById(courseId);

        if (existingCourse == null) {
            throw new CourseNotFoundException(
                    "Course not found with ID: " + courseId
            );
        }

        long activeEnrollmentCount =
                enrollmentRepository.findAll()
                        .stream()
                        .filter(enrollment ->
                                enrollment.getCourseId()
                                        .equals(courseId))
                        .filter(enrollment ->
                                "ENROLLED".equals(
                                        enrollment.getStatus()))
                        .count();

        if (request.getMaxCapacity()
                < activeEnrollmentCount) {

            throw new IllegalArgumentException(
                    "Maximum capacity cannot be less than "
                    + "current enrolled students: "
                    + activeEnrollmentCount
            );
        }

        existingCourse.setCourseName(
                request.getCourseName()
        );

        existingCourse.setTrainerName(
                request.getTrainerName()
        );

        existingCourse.setDurationInDays(
                request.getDurationInDays()
        );

        existingCourse.setMaxCapacity(
                request.getMaxCapacity()
        );

        existingCourse.setFees(
                request.getFees()
        );

        Course updatedCourse =
                courseRepository.save(existingCourse);

        return courseMapper.toResponseDto(updatedCourse);
    }

    @Override
    public void deleteCourse(Integer courseId) {

        Course course =
                courseRepository.findById(courseId);

        if (course == null) {
            throw new CourseNotFoundException(
                    "Course not found with ID: " + courseId
            );
        }

        courseRepository.deleteById(courseId);
    }

    @Override
    public List<CourseResponseDto> getCoursesByTrainer(
            String trainerName) {

        return courseRepository.findAll()
                .stream()
                .filter(course ->
                        course.getTrainerName()
                                .equalsIgnoreCase(trainerName))
                .map(courseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponseDto> getCoursesByFeesLessThan(
            Double amount) {

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero"
            );
        }

        return courseRepository.findAll()
                .stream()
                .filter(course ->
                        course.getFees() < amount)
                .map(courseMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}