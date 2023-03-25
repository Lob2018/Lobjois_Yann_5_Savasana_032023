package com.openclassrooms.starterjwt.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

	@Mock
	private UserRepository userRepository;

	@Test
	public void testFindByEmail() {
		// Create a test user
		User user = new User();
		user.setEmail("yoga@studio.com");
		user.setFirstName("Yoga");

		// Mock the repository to return the test user when findByEmail is called
		when(userRepository.findByEmail("yoga@studio.com")).thenReturn(Optional.of(user));

		// Call the findByEmail method and verify that it returns the test user
		Optional<User> result = userRepository.findByEmail("yoga@studio.com");
		assertThat(result).isNotEmpty();
		assertThat(result.get().getFirstName()).isEqualTo("Yoga");
	}

	@Test
	  public void testExistsByEmail() {
	    // Mock the repository to return true when existsByEmail is called with a known email
	    when(userRepository.existsByEmail("yoga@studio.com")).thenReturn(true);

	    // Call the existsByEmail method and verify that it returns true
	    boolean result = userRepository.existsByEmail("yoga@studio.com");
	    assertThat(result).isTrue();
	  }

}
