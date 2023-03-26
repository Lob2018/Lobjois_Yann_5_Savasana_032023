package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private SessionService sessionService;

	@Mock
	private SessionMapper sessionMapper;

	@Mock
	private SessionRepository sessionRepository;

	@InjectMocks
	private SessionController sessionController;

	private final LocalDateTime localDateTime = LocalDateTime.now();
	private final String email = "yoga@studio.com";
	private final String firstName = "Yoga";
	private final String lastName = "Studio";
	private final String password = "test!1234";
	private User user1;
	private User user2;
	private Teacher teacher;

	private List<User> users;
	private List<Session> sessions;
	private List<Long> usersIds;
	private SessionDto sessionDto;
	private SessionDto createdSessionDto;
	private Session session;
	private Long id = 1L;
	private Long teacher_id = 1L;
	private String name;
	private Date date = new Date();
	private String description = "The session 1 description";

	@BeforeEach
	public void init() {
		user1 = new User(email, lastName, firstName, password, false);
		user2 = new User(email, lastName, firstName, password, false);
		teacher = new Teacher(id, lastName, firstName, localDateTime, localDateTime);
		users = new ArrayList<User>(Arrays.asList(user1, user2));
		usersIds = new ArrayList<Long>(Arrays.asList(1L, 2L));
		sessions = new ArrayList<Session>(Arrays.asList(session, session));
		// Mock Session object
		session = new Session();
		session.setId(id);
		session.setCreatedAt(localDateTime);
		session.setDate(date);
		session.setDescription(description);
		session.setName(name);
		session.setTeacher(teacher);
		session.setUpdatedAt(localDateTime);
		session.setUsers(users);
		// Mock SessionDto object
		sessionDto = new SessionDto();
		sessionDto.setId(id);
		sessionDto.setName(name);
		sessionDto.setDate(date);
		sessionDto.setTeacher_id(teacher_id);
		sessionDto.setDescription(description);
		sessionDto.setUsers(usersIds);
		sessionDto.setCreatedAt(localDateTime);
		sessionDto.setUpdatedAt(localDateTime);
		// Mock the SessionDto object returne by sessionMapper
		createdSessionDto = new SessionDto();
		createdSessionDto.setId(id);
		createdSessionDto.setName(name);
		createdSessionDto.setDate(date);
		createdSessionDto.setTeacher_id(teacher_id);
		createdSessionDto.setDescription(description);
		createdSessionDto.setUsers(usersIds);
		createdSessionDto.setCreatedAt(localDateTime);
		createdSessionDto.setUpdatedAt(localDateTime);
	}

	@Test
	public void testFindById_success() {
		// GIVEN
		when(sessionService.getById(1L)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);
		// WHEN
		// Call the controller method
		ResponseEntity<?> responseEntity = sessionController.findById("1");
		// THEN
		// Assert the response status and body
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(sessionDto, responseEntity.getBody());
	}

	@Test
	public void testFindByIdNotFound_fail() {
		// GIVEN
		when(sessionService.getById(1L)).thenReturn(null);
		// WHEN
		// Call the controller method
		ResponseEntity<?> responseEntity = sessionController.findById("1");
		// THEN
		// Assert the response status and body		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@Test
	public void testFindByIdNotANumber_fail() {
		// GIVEN
		doThrow(NumberFormatException.class).when(sessionService).getById(anyLong());
		ResponseEntity<?> responseEntity = null;
		// WHEN
		try {
			responseEntity = sessionController.findById("invalid");
		} catch (NumberFormatException e) {
			// THEN
			verify(sessionService).getById(anyLong());
			Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		}
	}

	@Test
	public void testCreate_success() {
		// GIVEN
		when(sessionService.create(any(Session.class))).thenReturn(session);
		// Mock the session TODTO returned by the mapper
		when(sessionMapper.toDto(session)).thenReturn(createdSessionDto);		
		// Mock the session TOENTITY returned by the mapper
		when(sessionMapper.toEntity(createdSessionDto)).thenReturn(session);		
		// Call the controller method
		// WHEN
		ResponseEntity<?> responseEntity = sessionController.create(sessionDto);
		// THEN
		// Assert the response status and body
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());		
		assertEquals(createdSessionDto, responseEntity.getBody());
	}

	@Test
    public void testFindAll_success() {   
		// GIVEN
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(Arrays.asList(sessionDto, sessionDto));
        // WHEN        
        ResponseEntity<?> response = sessionController.findAll();
        // THEN
        verify(sessionService, times(1)).findAll();        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Arrays.asList(sessionDto, sessionDto));
    }

	@Test
    public void testUpdate_success() {
    	// GIVEN
        // Mock the sessionMapper.toEntity method to return the session object
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        // Mock the sessionService.update method to return the session object
        when(sessionService.update(1L, session)).thenReturn(session);
        // WHEN
        // Call the update method in the sessionController with the sample sessionDto object and assert the response
        ResponseEntity<?> responseEntity = sessionController.update("1", sessionDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // THEN
        // Verify that the sessionMapper.toDto method is called with the session object
        verify(sessionMapper, times(1)).toDto(session);
    }

	@Test
	public void testUpdateNotANumber_fail() {
		// GIVEN
		ArgumentCaptor<Session> argument = ArgumentCaptor.forClass(Session.class);
		doThrow(NumberFormatException.class).when(sessionService).update(anyLong(), argument.capture());
		ResponseEntity<?> responseEntity = null;
		// WHEN
		try {
			responseEntity = sessionController.update("invalid", sessionDto);
		} catch (NumberFormatException e) {
			// THEN
			verify(sessionService).getById(anyLong());
			Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		}
	}

	@Test
    void testSaveWithValidId_success() {
		// GIVEN
        when(sessionService.getById(id)).thenReturn(session);
        // WHEN
        ResponseEntity<?> response = sessionController.save(String.valueOf(id));
        // THEN
        verify(sessionService).delete(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

	@Test
	void testSaveNotFound_fail() {
		// GIVEN
		when(sessionService.getById(anyLong())).thenReturn(null);
		// WHEN
		ResponseEntity<?> response = sessionController.save("1");
		// THEN
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void testSaveWithNumberFormatException_fail() {
		// GIVEN
		when(sessionService.getById(anyLong())).thenThrow(NumberFormatException.class);
		// WHEN
		ResponseEntity<?> response = sessionController.save("invalid");
		// THEN
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testParticipate_success() {
		// GIVEN
		doNothing().when(sessionService).participate(anyLong(), anyLong());
		// WHEN
		ResponseEntity<?> response = sessionController.participate("1", "2");
		// THEN
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(sessionService).participate(1L, 2L);
	}

	@Test
	public void testParticipateInvalidId_fail() {
		// GIVEN
		doThrow(NumberFormatException.class).when(sessionService).participate(anyLong(), anyLong());
		ResponseEntity<?> response = null;
		// WHEN
		try {
			response = sessionController.participate("invalid", "2");
		} catch (NumberFormatException e) {
			// THEN
			verify(sessionService).participate(anyLong(), anyLong());
			Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		}
	}

	@Test
	public void testNoLongerParticipate_success() {
		// GIVEN
		String sessionId = "1";
		String userId = "2";
		Mockito.doNothing().when(sessionService).noLongerParticipate(Mockito.anyLong(), Mockito.anyLong());
		// WHEN
		ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId, userId);
		// THEN
		Mockito.verify(sessionService, Mockito.times(1)).noLongerParticipate(Mockito.anyLong(), Mockito.anyLong());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testNoLongerParticipateWithInvalidId_fail() {
		// GIVEN
		String sessionId = "invalid_id";
		String userId = "1";
		doThrow(NumberFormatException.class).when(sessionService).noLongerParticipate(anyLong(), anyLong());
		// WHEN
		ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId, userId);
		// THEN
		Mockito.verify(sessionService, Mockito.times(0)).noLongerParticipate(Mockito.anyLong(), Mockito.anyLong());
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

}
