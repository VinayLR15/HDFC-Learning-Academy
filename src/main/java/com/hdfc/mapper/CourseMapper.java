package com.hdfc.mapper;

import org.springframework.stereotype.Component;

import com.hdfc.dto.CourseRequestDto;
import com.hdfc.dto.CourseResponseDto;
import com.hdfc.entity.Course;

@Component	
public class CourseMapper {
	
	public Course toEntity(CourseRequestDto dto) {
		
		Course course = new Course();
		
		course.setCourseName(dto.getCourseName());
		course.setTrainerName(dto.getTrainerName());
		course.setDurationInDays(dto.getDurationInDays());
		course.setMaxCapacity(dto.getMaxCapacity());
		course.setFees(dto.getFees());
		
		return course;
	}
	
	public CourseResponseDto toResponseDto(Course course) {
		
		return new CourseResponseDto(
				course.getCourseId(),
				course.getCourseName(),
				course.getTrainerName(),
				course.getDurationInDays(),
				course.getMaxCapacity(),
				course.getFees()
		);
		
	}

}
