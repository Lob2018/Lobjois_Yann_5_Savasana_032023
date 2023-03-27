package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private final Long id = 1L;
	private final String email = "yoga@studio.com";
	private final String firstName = "Yoga";
	private final String lastName = "Studio";
	private final String password = "test!1234";
	private final LocalDateTime date = LocalDateTime.now();

	private User user = new User(id, email, lastName, firstName, password, false, date, date);

	@Test
	public void testDeleteUser_success() {
		// GIVEN
		Long userId = 123L;
		// WHEN
		userService.delete(userId);
		// THEN
		verify(userRepository).deleteById(userId);
	}

	@Test
	public void testFindUserById_success() {
		// GIVEN
		Long userId = 1L;
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		// WHEN
		User actualUser = userService.findById(userId);
		// THEN
		assertEquals(user, actualUser);
	}

}
