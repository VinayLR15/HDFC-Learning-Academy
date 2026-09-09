package com.hdfc.mapper;

import org.springframework.stereotype.Component;

import com.hdfc.dto.EnrollmentResponseDto;
import com.hdfc.entity.Enrollment;

@Component
public class EnrollmentMapper {
	
	public EnrollmentResponseDto toResponseDto(Enrollment enrollment) {
		
		return new EnrollmentResponseDto(
				enrollment.getEnrollmentId(),
				enrollment.getEmployeeId(),
				enrollment.getEmployeeName(),
				enrollment.getCourseId(),
				enrollment.getEnrollmentDate(),
				enrollment.getStatus()
		);
	}

}
