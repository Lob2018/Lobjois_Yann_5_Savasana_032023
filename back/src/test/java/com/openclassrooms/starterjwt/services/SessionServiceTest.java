package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class SessionServiceTest {

	@Mock
	private SessionRepository sessionRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private SessionService sessionService;

	private Session session;
	private User user;

	@BeforeEach
	public void setup() {
		session = new Session();
		session.setId(1L);
		session.setUsers(new ArrayList<User>());
		user = new User();
		user.setId(2L);
	}

	@Test
	public void testCreateSession_succes() {
		// GIVEN
		Session session = new Session();
		when(sessionRepository.save(session)).thenReturn(session);
		// WHEN
		Session createdSession = sessionService.create(session);
		// THEN
		assertNotNull(createdSession);
		verify(sessionRepository, times(1)).save(session);
	}

	@Test
	public void testDeleteSession_success() {
		// GIVEN
		Long sessionId = 1L;
		// WHEN
		sessionService.delete(sessionId);
		// THEN
		verify(sessionRepository, times(1)).deleteById(sessionId);
	}

	@Test
	public void testFindAllSessions_success() {
		// GIVEN
		List<Session> sessions = Arrays.asList(new Session(), new Session());
		when(sessionRepository.findAll()).thenReturn(sessions);
		// WHEN
		List<Session> foundSessions = sessionService.findAll();
		// THEN
		assertNotNull(foundSessions);
		assertEquals(sessions.size(), foundSessions.size());
		verify(sessionRepository, times(1)).findAll();
	}

	@Test
	public void testGetSessionById_success() {
		// GIVEN
		Long sessionId = 1L;
		Session session = new Session();
		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
		// WHEN
		Session foundSession = sessionService.getById(sessionId);
		// THEN
		assertNotNull(foundSession);
		verify(sessionRepository, times(1)).findById(sessionId);
	}

	@Test
	public void testUpdateSession_success() {
		// GIVEN
		Long sessionId = 1L;
		Session session = new Session();
		session.setId(sessionId);
		when(sessionRepository.save(session)).thenReturn(session);
		// WHEN
		Session updatedSession = sessionService.update(sessionId, session);
		// THEN
		assertNotNull(updatedSession);
		verify(sessionRepository, times(1)).save(session);
	}

	@Test
    public void testParticipate_success() {
    	// GIVEN
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        // WHEN
        sessionService.participate(1L, 2L);
        // THEN
        verify(sessionRepository, times(1)).findById(1L);     
        verify(userRepository, times(1)).findById(2L);
        verify(sessionRepository, times(1)).save(session);
    }

	@Test
    public void testParticipateWithNullSessionNotFoundExceptio_fail() {
		// GIVEN
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        // WHEN
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));
        // THEN
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, times(0)).save(any(Session.class));
    }

	@Test
    public void testParticipateWithNullUserNotFoundExceptio_fail() {
		// GIVEN
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        // WHEN
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));
        // THEN
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(sessionRepository, times(0)).save(any(Session.class));
    }

	@Test
	public void testParticipateAlreadyParticipatedBadRequestException_fail() {
		// GIVEN
		session.getUsers().add(user);
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
		when(userRepository.findById(2L)).thenReturn(Optional.of(user));
		// WHEN
		assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 2L));
		// THEN
		verify(sessionRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).findById(2L);
		verify(sessionRepository, times(0)).save(any(Session.class));
	}

	@Test
	public void testNoLongerParticipate_success() {
		// GIVEN
		session.getUsers().add(user);
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
		// WHEN
		sessionService.noLongerParticipate(1L, 2L);
		// THEN
		verify(sessionRepository, times(1)).findById(1L);
		verify(sessionRepository, times(1)).save(session);
	}

	@Test
	public void testNoLongerParticipateButWithOtherSessionParticipants_success() {
		// GIVEN
		User user2 = new User();
		user2.setId(3L);
		ArrayList<User> users = new ArrayList<User>();
		users.add(user);
		users.add(user2);
		session.setUsers(users);
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
		// WHEN
		sessionService.noLongerParticipate(1L, 2L);
		// THEN
		verify(sessionRepository, times(1)).findById(1L);
		verify(sessionRepository, times(1)).save(session);
	}

	@Test
	public void testNoLongerParticipateSessionNotFound_fail() {
		// GIVEN
		doReturn(Optional.empty()).when(sessionRepository).findById(anyLong());
		// WHEN
		assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 2L));
		// THEN
		verify(sessionRepository).findById(1L);
	}

	@Test
	public void testNoLongerParticipateUserNotParticipating_fail() {
		// GIVEN
		doReturn(Optional.of(session)).when(sessionRepository).findById(anyLong());
		// WHEN
		assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 2L));
		// THEN
		verify(sessionRepository).findById(1L);
	}

}
