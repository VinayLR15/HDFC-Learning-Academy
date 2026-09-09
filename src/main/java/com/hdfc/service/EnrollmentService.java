package com.hdfc.service;

import com.hdfc.dto.EnrollmentRequestDto;
import com.hdfc.dto.EnrollmentResponseDto;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponseDto enrollEmployee(
            EnrollmentRequestDto request
    );

    List<EnrollmentResponseDto> getAllEnrollments();

    EnrollmentResponseDto getEnrollmentById(
            Integer enrollmentId
    );

    EnrollmentResponseDto cancelEnrollment(
            Integer enrollmentId
    );

    EnrollmentResponseDto completeEnrollment(
            Integer enrollmentId
    );

    List<EnrollmentResponseDto> getEnrollmentsByStatus(
            String status
    );

    List<EnrollmentResponseDto> getEnrollmentsByEmployeeId(
            Integer employeeId
    );

    long getEnrollmentCount();

    EnrollmentResponseDto getMostPopularCourse();
}