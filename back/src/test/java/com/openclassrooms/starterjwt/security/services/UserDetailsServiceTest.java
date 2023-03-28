package com.openclassrooms.starterjwt.security.services;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class UserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UsernameNotFoundException usernameNotFoundException;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	private final String mail = "yoga@studio.com";
	private final LocalDateTime localDateTime = LocalDateTime.now();
	private final Long id = 1L;
	private final String firstName = "Yoga";
	private final String lastName = "Studio";
	private final String password = "test!1234";
	private final User user = new User(id, mail, lastName, firstName, password, false, localDateTime, localDateTime);

	@Test
	public void loadUserByUsername_UserFound_ReturnsUserDetails() {
		// GIVEN
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		// WHEN
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		// THEN
		assertEquals(user.getEmail(), userDetails.getUsername());
		assertEquals(user.getFirstName(), ((UserDetailsImpl) userDetails).getFirstName());
		assertEquals(user.getLastName(), ((UserDetailsImpl) userDetails).getLastName());
		assertEquals(user.getPassword(), userDetails.getPassword());
	}

	@Test
	public void loadUserByUsername_UserNotFound_ThrowsException() {
		// GIVEN
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
		try {
			// WHEN
		    userRepository.findByEmail("test@example.com");
		} catch (UsernameNotFoundException e) {
			// THEN
		    verify(userRepository, times(1)).findByEmail("test@example.com");
		    assertEquals("User Not Found with email: test@example.com", e.getMessage());
		}
	}
}
