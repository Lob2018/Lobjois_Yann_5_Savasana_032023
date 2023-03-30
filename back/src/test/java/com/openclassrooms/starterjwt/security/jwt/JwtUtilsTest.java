package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@SpringBootTest
public class JwtUtilsTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@InjectMocks
	private JwtUtils jwtUtils;

	private User user;
	private UserDetailsImpl userDetailsImpl;
	private final Long id = 1L;
	private final String email = "yoga@studio.com";
	private final String firstName = "Yoga";
	private final String lastName = "Studio";
	private final String password = "test!1234";

	private static final String jwtSecret = "openclassrooms";
	private static final int jwtExpirationMs = 86400000;

	private final LocalDateTime date = LocalDateTime.now();

	@BeforeEach
	public void init() throws Exception {
		// Mock User object
		user = new User(id, email, lastName, firstName, password, false, date, date);
		// Mock UserDetailsImpl object
		userDetailsImpl = new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),
				user.isAdmin(), user.getPassword());
		// Mock private fields
		ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
		ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
	}

	@Test
	public void testGetUserNameFromJwtToken_success() {
		// GIVEN
		// mock the Authentication object
		Authentication authentication = mock(Authentication.class);
		// mock the authentication.getPrincipal method to return userDetailsImpl
		when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
		String token = jwtUtils.generateJwtToken(authentication);
		// WHEN
		String username = jwtUtils.getUserNameFromJwtToken(token);
		// THEN
		assertEquals(userDetailsImpl.getUsername(), username);
	}

	@Test
	public void testValidateJwtToken_success() {
		// GIVEN
		String validToken = Jwts.builder().setSubject(userDetailsImpl.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
				.signWith(SignatureAlgorithm.HS512, "openclassrooms").compact();
		// WHEN THEN
		assertTrue(jwtUtils.validateJwtToken(validToken));
	}

	@Test
	public void testValidateJwtTokenExpired_fail() {
		// GIVEN
		ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 0);
		// mock the Authentication object
		Authentication authentication = mock(Authentication.class);
		// mock the authentication.getPrincipal method to return userDetailsImpl
		when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
		String token = jwtUtils.generateJwtToken(authentication);
		// WHEN THEN
		assertFalse(jwtUtils.validateJwtToken(token));
	}

	@Test
	public void testValidateJwtTokenMalformed_fail() {
		// GIVEN
		// mock the Authentication object
		Authentication authentication = mock(Authentication.class);
		// mock the authentication.getPrincipal method to return userDetailsImpl
		when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
		// WHEN THEN
		assertFalse(jwtUtils.validateJwtToken("token"));
	}
	
	@Test
	public void testValidateJwtTokenIllegalArgumentException_fail() {
		// GIVEN
		// mock the Authentication object
		Authentication authentication = mock(Authentication.class);
		// mock the authentication.getPrincipal method to return userDetailsImpl
		when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
		// WHEN THEN
		assertFalse(jwtUtils.validateJwtToken(""));
	}
}
