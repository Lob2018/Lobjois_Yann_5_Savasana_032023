package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@SpringBootTest
public class TeacherServiceTest {

	@Mock
	private TeacherRepository teacherRepository;

	@InjectMocks
	private TeacherService teacherService;

	private Teacher teacher1;
	private Teacher teacher2;
	private Long id = 1L;
	private final LocalDateTime localDateTime = LocalDateTime.now();
	private final String firstName = "John";
	private final String lastName = "Studio";

	@BeforeEach
	public void init() {
		teacher1 = new Teacher(id, lastName, firstName, localDateTime, localDateTime);
		teacher2 = new Teacher(2L, lastName, "Jane", localDateTime, localDateTime);
	}

	@Test
	public void testFindAll_success() {
		// GIVEN
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher1);
		teachers.add(teacher2);
		when(teacherRepository.findAll()).thenReturn(teachers);
		// WHEN
		List<Teacher> result = teacherService.findAll();
		// THEN
		assertEquals(2, result.size());
		assertEquals("John", result.get(0).getFirstName());
		assertEquals("Jane", result.get(1).getFirstName());
	}

	@Test
	public void testFindById_success() {
		// GIVEN
		when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));
		// WHEN
		Teacher result = teacherService.findById(1L);
		// THEN
		assertEquals("John", result.getFirstName());
	}

}
