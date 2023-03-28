package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTest {
	@Mock
	private GrantedAuthority authority;

	private UserDetailsImpl userDetailsImpl;

	private Long id = 1L;
	private String username = "user";
	private String firstName = "John";
	private String lastName = "Doe";
	private boolean admin = true;
	private String password = "password";

	@BeforeEach
	public void setUp() {
		userDetailsImpl = UserDetailsImpl.builder().id(id).username(username).firstName(firstName).lastName(lastName)
				.admin(admin).password(password).build();
	}

	@Test
	void testGetPassword() {
		String password = "password";
		userDetailsImpl = UserDetailsImpl.builder().password(password).build();
		assertEquals(password, userDetailsImpl.getPassword());
	}

	@Test
	void testIsAdmin() {
		boolean admin = true;
		userDetailsImpl = UserDetailsImpl.builder().admin(admin).build();
		assertEquals(admin, userDetailsImpl.getAdmin());
	}

	@Test
	public void testGetAuthorities_success() {
		Collection<? extends GrantedAuthority> authorities = userDetailsImpl.getAuthorities();
		assertNotNull(authorities);
		assertTrue(authorities.isEmpty());
	}

	@Test
	public void testIsAccountNonExpired_success() {
		assertTrue(userDetailsImpl.isAccountNonExpired());
	}

	@Test
	public void testIsAccountNonLocked_success() {
		assertTrue(userDetailsImpl.isAccountNonLocked());
	}

	@Test
	public void testIsCredentialsNonExpired_success() {
		assertTrue(userDetailsImpl.isCredentialsNonExpired());
	}

	@Test
	public void testIsEnabled_success() {
		assertTrue(userDetailsImpl.isEnabled());
	}

	@Test
	public void testEqualsSameInstance_success() {
		UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
		UserDetailsImpl user2 = user1;
		assertTrue(user1.equals(user2));
		assertFalse(user1.equals(null));
		assertFalse(user1.equals("string"));
	}

	@Test
	public void testEqualsSameInstance_fail() {
		UserDetailsImpl user1 = UserDetailsImpl.builder().id(id).username(username).firstName(firstName)
				.lastName(lastName).admin(admin).password(password).build();
		assertTrue(userDetailsImpl.equals(user1));
		assertFalse(userDetailsImpl.equals(null));
		assertFalse(userDetailsImpl.equals("string"));
	}
}
