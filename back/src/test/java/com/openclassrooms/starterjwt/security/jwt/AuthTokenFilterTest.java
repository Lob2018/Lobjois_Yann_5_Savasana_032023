package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@SpringBootTest
public class AuthTokenFilterTest {

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private UserDetailsServiceImpl userDetailsService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@InjectMocks
	private AuthTokenFilter authTokenFilter;

	@BeforeEach
	public void init() throws JsonMappingException, JsonProcessingException {
	}

	@Test
	public void doFilterInternalValidJwtTokenShouldAuthenticateUser_success() throws ServletException, IOException {
		// GIVEN
		String jwtToken = "valid.jwt.token";
		String username = "testuser";
		// mock the behavior of the JwtUtils class
		when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(true);
		when(jwtUtils.getUserNameFromJwtToken(jwtToken)).thenReturn(username);
		// mock the behavior of the UserDetailsService class
		UserDetails userDetails = new User(username, "", new ArrayList<>());
		when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
		// mock the behavior of the HttpServletRequest class
		when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
		// WHEN
		// call the doFilterInternal method of the AuthTokenFilter class
		authTokenFilter.doFilterInternal(request, response, filterChain);
		// THEN
		// assert that the user is authenticated
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		assertNotNull(authentication);
		UserDetails ud = (UserDetails) authentication.getPrincipal();
		assertEquals(username, ud.getUsername());
	}
}
