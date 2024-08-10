package com.techlabs.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.CourseDTO;
import com.techlabs.app.dto.StudentDTO;
import com.techlabs.app.entity.Course;
import com.techlabs.app.entity.Student;
import com.techlabs.app.repository.CourseRepository;
import com.techlabs.app.repository.StudentRepository;


@Service
public class StudentServiceImpl implements StudentService{
	
	private StudentRepository studentRepository;
	
	private CourseRepository courseRepository;

	public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
		super();
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public Course createCourse(Course course) {
		course.setId((long) 0);
		return courseRepository.save(course);
	}

	@Override
	public List<CourseDTO> getAllCourses() {
		return courseRepository.findAll().stream()
				.map(this::toDTO)
	            .collect(Collectors.toList());
	}

	@Override
	public CourseDTO getCourseById(Long id) {
		Course course =  courseRepository.findById(id).orElse(null);
		if(course != null) {
			return toDTO(course);
		}
		return null;
		
	}

	@Override
	public void deleteCourse(Long id) {
		Course course = courseRepository.findById(id).orElse(null);
		if(course != null) courseRepository.delete(course);
		
	}

	@Override
	public Student createStudent(Student student) {
		student.setId((long) 0);
		return studentRepository.save(student);
	}

	@Override
	public List<StudentDTO> getAllStudents() {
		return studentRepository.findAll().stream()
				.map(this::toDTO)
	            .collect(Collectors.toList());
	}

	@Override
	public StudentDTO getStudentById(Long id) {
		Student student = studentRepository.findById(id).orElse(null);
		if(student != null) {
			return toDTO(student);
		}
		return null;
	}

	@Override
	public void deleteStudent(Long id) {
		Student student = studentRepository.findById(id).orElse(null);
		if(student != null) studentRepository.delete(student);
		
	}

	@Override
	public Student addCourseToStudent(Long studentId, Long courseId) {
		Student student = studentRepository.findById(studentId).orElse(null);
		if(student != null) {
			Course course = courseRepository.findById(courseId).orElse(null);
			if(course != null) {
				student.getCourses().add(course);
//		        course.getStudents().add(student);
//		        courseRepository.save(course);
				return studentRepository.save(student);
			}
		}
        return null;
        
	}

	@Override
	public Student removeCourseFromStudent(Long studentId, Long courseId) {
		Student student = studentRepository.findById(studentId).orElse(null);
		if(student != null) {
			Course course = courseRepository.findById(courseId).orElse(null);
			if(course != null) {
				student.getCourses().remove(course);
				return studentRepository.save(student);
			}
		}
        return null;
	}
	
	public CourseDTO toDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setStudents(course.getStudents().stream()
            .map(student -> new StudentDTO(student.getId(), student.getName()))
            .collect(Collectors.toSet()));
        return courseDTO;
    }
	
	public StudentDTO toDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setCourses(student.getCourses().stream()
            .map(course -> new CourseDTO(course.getId(), course.getTitle()))
            .collect(Collectors.toSet()));
        return studentDTO;
    }

}
