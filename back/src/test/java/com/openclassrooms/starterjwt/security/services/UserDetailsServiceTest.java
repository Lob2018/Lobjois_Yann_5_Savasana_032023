package com.openclassrooms.starterjwt.security.services;

import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
	public void testLoadUserByUsername_success() {
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
	public void testLoadUserByUsernameThrowsUserNotFound_fail() {
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
