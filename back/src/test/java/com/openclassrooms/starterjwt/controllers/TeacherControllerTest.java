package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.services.TeacherService;

import org.springframework.http.ResponseEntity;

@SpringBootTest
public class TeacherControllerTest {

	@Mock
	private TeacherService teacherService;

	@Mock
	private TeacherMapper teacherMapper;

	@InjectMocks
	private TeacherController teacherController;

	private Long id = 1L;
	private final LocalDateTime localDateTime = LocalDateTime.now();
	private final String firstName = "Yoga";
	private final String lastName = "Studio";
	private Teacher teacher1;
	private TeacherDto teacher1Dto;
	private Teacher teacher2;
	private TeacherDto teacher2Dto;
	private List<Teacher> teachers;
	private List<TeacherDto> teachersDto;

	@BeforeEach
	public void init() {
		teacher1 = new Teacher(id, lastName, firstName, localDateTime, localDateTime);
		teacher1Dto = new TeacherDto(id, lastName, firstName, localDateTime, localDateTime);
		teacher2 = new Teacher(2L, lastName, firstName, localDateTime, localDateTime);
		teacher2Dto = new TeacherDto(2L, lastName, firstName, localDateTime, localDateTime);
		teachers = new ArrayList<Teacher>(Arrays.asList(teacher1, teacher2));
		teachersDto = new ArrayList<TeacherDto>(Arrays.asList(teacher1Dto, teacher2Dto));
	}

	@Test
	public void testFindById_success() throws Exception {
		// GIVEN		
		when(teacherService.findById(id)).thenReturn(teacher1);
		when(teacherMapper.toDto(teacher1)).thenReturn(teacher1Dto);
		// WHEN
		ResponseEntity<?> response = teacherController.findById(id.toString());
		// THEN
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(teacher1Dto);
	}

	@Test
	public void testFindByIdNotFound_fail() throws Exception {
		// GIVEN		
		when(teacherService.findById(id)).thenReturn(null);
		when(teacherMapper.toDto(teacher1)).thenReturn(null);
		// WHEN
		ResponseEntity<?> response = teacherController.findById(id.toString());
		// THEN
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testFindByIdNotANumber_fail() throws Exception {
		// GIVEN
		doThrow(NumberFormatException.class).when(teacherService).findById(anyLong());
		ResponseEntity<?> responseEntity = null;
		// WHEN
		try {
			responseEntity = teacherController.findById("invalid");
		} catch (NumberFormatException e) {
			// THEN
			verify(teacherService).findById(anyLong());
			Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		}
	}

	@Test
	public void testFindAll_success() throws Exception {
		// GIVEN	
		when(teacherService.findAll()).thenReturn(teachers);
		when(teacherMapper.toDto(teachers)).thenReturn(teachersDto);
		// WHEN
		ResponseEntity<?> response = teacherController.findAll();
		// THEN
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(teachersDto);
	}
}
