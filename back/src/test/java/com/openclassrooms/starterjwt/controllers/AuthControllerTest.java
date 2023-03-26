package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private AuthController authController;

	private SignupRequest signupRequest;
	private LoginRequest loginRequest;
	private User user;
	private UserDetailsImpl userDetailsImpl;

	private final Long id = 1L;
	private final String email = "yoga@studio.com";
	private final String firstName = "Yoga";
	private final String lastName = "Studio";
	private final String password = "test!1234";
	private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE2NzkzNDcxNDksImV4cCI6MTY3OTQzMzU0OX0.LMLUeOwdXzY42N1AQRo2xPiBppbXmjSHDqS73EuxSb1zoOwjlRQiMt92COFPgb0_QbPk-nBCRDy075a9kA0Ptg";

	private final LocalDateTime date = LocalDateTime.now();

	@BeforeEach
	public void init() {
		// Mock SignupRequest object
		signupRequest = new SignupRequest();
		signupRequest.setEmail(email);
		signupRequest.setFirstName(firstName);
		signupRequest.setLastName(lastName);
		signupRequest.setPassword(password);
		// Mock LoginRequest object
		loginRequest = new LoginRequest();
		loginRequest.setEmail(email);
		loginRequest.setPassword(password);
		// Mock User object
		user = new User(id, email, lastName, firstName, password, false, date, date);
		// Mock UserDetailsImpl object
		userDetailsImpl = new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),
				user.isAdmin(), user.getPassword());
	}

	@Test
	public void testRegisterUser_success() {
		// GIVEN 		
		// mock the UserRepository.existsByEmail method to return false
		when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		// mock the UserRepository.save method to return the User object passed in as an argument
		when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		// mock the PasswordEncoder.encode method to return the same string passed in as an argument
		when(passwordEncoder.encode(Mockito.anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
		// WHEN
		// call the registerUser method with the mock SignupRequest object
		ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);
		// THEN
		// assert that the UserRepository.existsByEmail method was called once with the correct email argument
		verify(userRepository, Mockito.times(1)).existsByEmail(email);
		// assert that the UserRepository.save method was called once with the correct User object argument
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository, Mockito.times(1)).save(userCaptor.capture());
		User savedUser = userCaptor.getValue();
		assertEquals(email, savedUser.getEmail());
		assertEquals(firstName, savedUser.getFirstName());
		assertEquals(lastName, savedUser.getLastName());
		assertEquals(password, savedUser.getPassword());
		// assert that the response entity has status code 200 and the correct message response body
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("User registered successfully!", ((MessageResponse) responseEntity.getBody()).getMessage());
	}

	@Test
	public void testRegisterUser_fail() {
		// GIVEN 		
		// mock the UserRepository.existsByEmail method to return true
		when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
		// WHEN
		// call the registerUser method with the mock SignupRequest object
		ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);
		// THEN
		// assert that the UserRepository.existsByEmail method was called once with the incorrect email argument
		verify(userRepository, Mockito.times(1)).existsByEmail(email);		
		// assert that the UserRepository.save method was not called with the incorrect User object argument
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository, Mockito.times(0)).save(userCaptor.capture());		
		// assert that the response entity has status code 400 and the error message response body		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Error: Email is already taken!", ((MessageResponse) responseEntity.getBody()).getMessage());		
	}

	@Test
	public void testAuthenticateUser_success() {
		// GIVEN
		// mock the Authentication object
		Authentication authentication = mock(Authentication.class);
		// mock the authenticationManager.authenticate method to return authentication
		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		// mock the authentication.getPrincipal method to return userDetailsImpl
		when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
		// mock the jwtUtils.generateJwtToken method to return token
		when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);
		// mock the UserRepository.findByEmail method to return user
		Optional<User> opt = Optional.of(user);
		when(userRepository.findByEmail(userDetailsImpl.getUsername())).thenReturn(opt);
		// WHEN
		ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);
		JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
		// THEN
		assertNotNull(jwtResponse.getToken());
		assertNotNull(jwtResponse.getId());
		assertEquals(user.getEmail(), jwtResponse.getUsername());
		assertEquals(user.getFirstName(), jwtResponse.getFirstName());
		assertEquals(user.getLastName(), jwtResponse.getLastName());
		assertFalse(jwtResponse.getAdmin());
		// assert that the response entity has status code 200 and the correct message
		// response body
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	void testAuthenticateUserNotAdmin_success() {
		// GIVEN
		// mock the Authentication object
		Authentication authentication = mock(Authentication.class);
		// mock the authenticationManager.authenticate method to return authentication
		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		// mock the authentication.getPrincipal method to return userDetailsImpl
		when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
		// mock the jwtUtils.generateJwtToken method to return token
		when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);		
		// mock the UserRepository.findByEmail method to return null
		Optional<User> opt = Optional.ofNullable(null);		
		when(userRepository.findByEmail(email)).thenReturn(opt);
		// WHEN
		ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);
		// THEN
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

}
