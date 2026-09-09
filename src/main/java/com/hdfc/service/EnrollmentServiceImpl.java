package com.hdfc.service;

import com.hdfc.dto.EnrollmentRequestDto;
import com.hdfc.dto.EnrollmentResponseDto;
import com.hdfc.entity.Course;
import com.hdfc.entity.Enrollment;
import com.hdfc.exception.CourseCapacityFullException;
import com.hdfc.exception.CourseNotFoundException;
import com.hdfc.exception.DuplicateEnrollmentException;
import com.hdfc.exception.EnrollmentNotFoundException;
import com.hdfc.mapper.EnrollmentMapper;
import com.hdfc.repository.CourseRepository;
import com.hdfc.repository.EnrollmentRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl
        implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    private final AtomicInteger enrollmentIdGenerator =
            new AtomicInteger(0);

    public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository,
            EnrollmentMapper enrollmentMapper) {

        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    @Override
    public EnrollmentResponseDto enrollEmployee(
            EnrollmentRequestDto request) {

        Course course =
                courseRepository.findById(
                        request.getCourseId()
                );

        if (course == null) {

            throw new CourseNotFoundException(
                    "Course not found with ID: "
                    + request.getCourseId()
            );
        }

        boolean duplicate =
                enrollmentRepository.findAll()
                        .stream()
                        .anyMatch(enrollment ->
                                enrollment.getEmployeeId()
                                        .equals(request.getEmployeeId())
                                &&
                                enrollment.getCourseId()
                                        .equals(request.getCourseId())
                        );

        if (duplicate) {

            throw new DuplicateEnrollmentException(
                    "Employee "
                    + request.getEmployeeId()
                    + " has already enrolled in course "
                    + request.getCourseId()
            );
        }

        long currentEnrolledCount =
                enrollmentRepository.findAll()
                        .stream()
                        .filter(enrollment ->
                                enrollment.getCourseId()
                                        .equals(request.getCourseId()))
                        .filter(enrollment ->
                                "ENROLLED".equals(
                                        enrollment.getStatus()))
                        .count();

        if (currentEnrolledCount
                >= course.getMaxCapacity()) {

            throw new CourseCapacityFullException(
                    "Course capacity is full for course ID: "
                    + request.getCourseId()
            );
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setEnrollmentId(
                enrollmentIdGenerator.incrementAndGet()
        );

        enrollment.setEmployeeId(
                request.getEmployeeId()
        );

        enrollment.setEmployeeName(
                request.getEmployeeName()
        );

        enrollment.setCourseId(
                request.getCourseId()
        );

        enrollment.setEnrollmentDate(
                LocalDate.now()
        );

        enrollment.setStatus("ENROLLED");

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponseDto(
                savedEnrollment
        );
    }

    @Override
    public List<EnrollmentResponseDto> getAllEnrollments() {

        return enrollmentRepository.findAll()
                .stream()
                .map(enrollmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentResponseDto getEnrollmentById(
            Integer enrollmentId) {

        Enrollment enrollment =
                enrollmentRepository.findById(enrollmentId);

        if (enrollment == null) {

            throw new EnrollmentNotFoundException(
                    "Enrollment not found with ID: "
                    + enrollmentId
            );
        }

        return enrollmentMapper.toResponseDto(
                enrollment
        );
    }

    @Override
    public EnrollmentResponseDto cancelEnrollment(
            Integer enrollmentId) {

        Enrollment enrollment =
                getEnrollmentEntity(enrollmentId);

        if ("COMPLETED".equals(enrollment.getStatus())) {

            throw new IllegalArgumentException(
                    "Completed enrollment cannot be cancelled"
            );
        }

        if ("CANCELLED".equals(enrollment.getStatus())) {

            throw new IllegalArgumentException(
                    "Enrollment is already cancelled"
            );
        }

        enrollment.setStatus("CANCELLED");

        enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponseDto(
                enrollment
        );
    }

    @Override
    public EnrollmentResponseDto completeEnrollment(
            Integer enrollmentId) {

        Enrollment enrollment =
                getEnrollmentEntity(enrollmentId);

        if ("CANCELLED".equals(enrollment.getStatus())) {

            throw new IllegalArgumentException(
                    "Cancelled enrollment cannot be completed"
            );
        }

        if ("COMPLETED".equals(enrollment.getStatus())) {

            throw new IllegalArgumentException(
                    "Enrollment is already completed"
            );
        }

        enrollment.setStatus("COMPLETED");

        enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponseDto(
                enrollment
        );
    }

    @Override
    public List<EnrollmentResponseDto> getEnrollmentsByStatus(
            String status) {

        validateStatus(status);

        return enrollmentRepository.findAll()
                .stream()
                .filter(enrollment ->
                        enrollment.getStatus()
                                .equalsIgnoreCase(status))
                .map(enrollmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponseDto> getEnrollmentsByEmployeeId(
            Integer employeeId) {

        if (employeeId == null || employeeId <= 0) {

            throw new IllegalArgumentException(
                    "Employee ID must be positive"
            );
        }

        return enrollmentRepository.findAll()
                .stream()
                .filter(enrollment ->
                        enrollment.getEmployeeId()
                                .equals(employeeId))
                .map(enrollmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getEnrollmentCount() {

        return enrollmentRepository.findAll()
                .stream()
                .count();
    }

    @Override
    public EnrollmentResponseDto getMostPopularCourse() {

        List<Enrollment> enrollments =
                enrollmentRepository.findAll();

        if (enrollments.isEmpty()) {
            return null;
        }

        Integer mostPopularCourseId =
                enrollments.stream()
                        .collect(
                                Collectors.groupingBy(
                                        Enrollment::getCourseId,
                                        Collectors.counting()
                                )
                        )
                        .entrySet()
                        .stream()
                        .max(
                                java.util.Map.Entry
                                        .comparingByValue()
                        )
                        .map(java.util.Map.Entry::getKey)
                        .orElse(null);

        Enrollment representativeEnrollment =
                enrollments.stream()
                        .filter(enrollment ->
                                enrollment.getCourseId()
                                        .equals(mostPopularCourseId))
                        .findFirst()
                        .orElse(null);

        return enrollmentMapper.toResponseDto(
                representativeEnrollment
        );
    }

    private Enrollment getEnrollmentEntity(
            Integer enrollmentId) {

        Enrollment enrollment =
                enrollmentRepository.findById(enrollmentId);

        if (enrollment == null) {

            throw new EnrollmentNotFoundException(
                    "Enrollment not found with ID: "
                    + enrollmentId
            );
        }

        return enrollment;
    }

    private void validateStatus(String status) {

        if (status == null ||
                (!status.equalsIgnoreCase("ENROLLED")
                && !status.equalsIgnoreCase("COMPLETED")
                && !status.equalsIgnoreCase("CANCELLED"))) {

            throw new IllegalArgumentException(
                    "Invalid status. Allowed values: "
                    + "ENROLLED, COMPLETED, CANCELLED"
            );
        }
    }
}