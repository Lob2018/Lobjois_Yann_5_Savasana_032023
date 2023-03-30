package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();
	private JsonNode jsonNode;

	@BeforeEach
	public void init() throws JsonMappingException, JsonProcessingException {
		jsonNode = objectMapper.readTree("");
	}

	@Test
	public void testAuthLogin_success() throws Exception {
		// GIVEN
		String json = "{\"email\": \"yoga@studio.com\", \"password\": \"test!1234\"}";
		// WHEN THEN
		MvcResult result = mockMvc
				.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn();
		String token = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(token);
		String userName = jsonNode.get("username").asText();
		assertEquals(userName, "yoga@studio.com");
	}

	@Test
	public void testAuthLoginUnauthorized_fail() throws Exception {
		// GIVEN
		String json = "{\"email\": \"yoga@studio.com\", \"password\": \"test!123\"}";
		// WHEN THEN
		mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testAuthRegister_success() throws Exception {
		// GIVEN
		String json = "{\"lastName\": \"toto\", \"firstName\": \"test!123\", \"email\": \"toto3@toto.com\", \"password\": \"test!1234\"}";
		// WHEN THEN
		MvcResult result = mockMvc
				.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn();
		String message = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(message);
		assertEquals(jsonNode.get("message").asText(), "User registered successfully!");
	}

	@Test
	public void testAuthRegisterEmailAlreadyTokend_fail() throws Exception {
		// GIVEN
		String json = "{\"lastName\": \"toto\", \"firstName\": \"test!123\", \"email\": \"yoga@studio.com\", \"password\": \"test!1234\"}";
		// WHEN THEN
		MvcResult result = mockMvc
				.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andReturn();
		String message = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(message);
		assertEquals(jsonNode.get("message").asText(), "Error: Email is already taken!");
	}

}
