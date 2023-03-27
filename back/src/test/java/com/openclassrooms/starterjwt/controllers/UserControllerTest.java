package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
public class UserControllerTest {

	@Mock
	private UserService userService;

	@Mock
	private UserMapper userMapper;

	@Mock
	private Authentication authentication;

	@Mock
	private AuthenticationManager authenticationManager;

	@InjectMocks
	private UserController userController;

	private final LocalDateTime localDateTime = LocalDateTime.now();
	private final Long id = 1L;
	private final String email = "yoga@studio.com";
	private final String firstName = "Yoga";
	private final String lastName = "Studio";
	private final String password = "test!1234";
	private final User user = new User(id, email, lastName, firstName, password, false, localDateTime, localDateTime);
	private final User anotherUser = new User(2L, "yoga2@studio.com", lastName, firstName, password, false,
			localDateTime, localDateTime);
	private UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(),
			user.getLastName(), user.isAdmin(), user.getPassword());

	@Test
	public void testFindById_success() {
		// GIVEN
		long id = 1L;
		User user = new User();
		user.setId(id);
		when(userService.findById(id)).thenReturn(user);
		when(userMapper.toDto(user)).thenReturn(new UserDto());
		// WHEN
		ResponseEntity<?> response = userController.findById(String.valueOf(id));
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		verify(userService).findById(id);
		verify(userMapper).toDto(user);
	}

	@Test
	public void testFindByIdNotFound_fail() {
		// GIVEN
		long id = 1L;
		User user = new User();
		user.setId(id);
		when(userService.findById(id)).thenReturn(null);
		// WHEN
		ResponseEntity<?> response = userController.findById(String.valueOf(id));
		// THEN
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
		verify(userService).findById(id);
	}

	@Test
	public void testFindByIdNotANumber_fail() {
		// GIVEN
		doThrow(NumberFormatException.class).when(userService).findById(anyLong());
		ResponseEntity<?> response = null;
		// WHEN
		try {
			response = userController.findById("invalid");
		} catch (NumberFormatException e) {
			// THEN
			verify(userService).findById(anyLong());
			Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		}
	}

	@Test
	public void testDeleteUser_success() {
		// GIVEN
		// mock the Authentication object
		Authentication authentication = mock(Authentication.class);
		// mock the SecurityContext object
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetailsImpl);
		when(userService.findById(1L)).thenReturn(user);
		// WHEN
		ResponseEntity<?> response = userController.save("1");
		// THEN
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(userService, times(1)).delete(1L);
	}

	@Test
	public void testDeleteUserNotFound_fail() {
		// GIVEN
		// mock the Authentication object
		Authentication authentication = mock(Authentication.class);
		// mock the SecurityContext object
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetailsImpl);
		when(userService.findById(1L)).thenReturn(null);
		// WHEN
		ResponseEntity<?> response = userController.save("1");
		// THEN
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
		verify(userService).findById(1L);
	}

	@Test
	public void testDeleteUserNotGood_fail() {
		// GIVEN
		// mock the Authentication object
		Authentication authentication = mock(Authentication.class);
		// mock the SecurityContext object
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetailsImpl);
		when(userService.findById(2L)).thenReturn(anotherUser);
		// WHEN
		ResponseEntity<?> response = userController.save("2");
		// THEN
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertNull(response.getBody());
		verify(userService).findById(2L);
	}

	@Test
	public void testDeleteUserNotANumber_fail() {
		// GIVEN
		doThrow(NumberFormatException.class).when(userService).findById(anyLong());
		ResponseEntity<?> response = null;
		// WHEN
		try {
			response = userController.save("invalid");
		} catch (NumberFormatException e) {
			// THEN
			verify(userService).findById(anyLong());
			Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		}
	}
}
