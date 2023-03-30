package com.openclassrooms.starterjwt.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;

@SpringBootTest
public class UserRepositoryTest {

	@Mock
	private UserRepository userRepository;

	private final String email = "yoga@studio.com";
	private final String firstName = "Yoga";

	@Test
	public void testFindByEmail_success() {
		// GIVEN
		// Create a test user
		User user = new User();
		user.setEmail(email);
		user.setFirstName(firstName);
		// Mock the repository to return the test user when findByEmail is called
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		// WHEN
		// Call the findByEmail method and verify that it returns the test user
		Optional<User> result = userRepository.findByEmail(email);
		// THEN
		assertThat(result).isNotEmpty();
		assertThat(result.get().getFirstName()).isEqualTo(firstName);
	}

	@Test
	  public void testExistsByEmail_success() {
		// GIVEN
	    // Mock the repository to return true when existsByEmail is called with a known email
	    when(userRepository.existsByEmail(email)).thenReturn(true);
	    // WHEN
	    // Call the existsByEmail method and verify that it returns true	   
	    boolean result = userRepository.existsByEmail(email);
	     // THEN
	    assertThat(result).isTrue();
	  }
}
