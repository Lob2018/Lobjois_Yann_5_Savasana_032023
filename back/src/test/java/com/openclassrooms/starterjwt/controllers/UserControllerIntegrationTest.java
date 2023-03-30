package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.security.test.context.support.WithMockUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();
	private JsonNode jsonNode;

	@BeforeEach
	public void init() throws JsonMappingException, JsonProcessingException {
		jsonNode = objectMapper.readTree("");
	}

	@Test
	public void testUserFindByIdUnauthorized_fail() throws Exception {
		// GIVEN WHEN
		MvcResult result = mockMvc.perform(get("/api/user/1")).andExpect(status().isUnauthorized()).andReturn();
		jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
		// THEN
		assertEquals(jsonNode.get("message").asText(), "Full authentication is required to access this resource");
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testUserFindByIdNotFound_fail() throws Exception {
		// GIVEN WHEN THEN
		mockMvc.perform(get("/api/user/99999")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testUserFindById_success() throws Exception {
		// GIVEN WHEN
		MvcResult result = mockMvc.perform(get("/api/user/1")).andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
		// THEN
		assertEquals(jsonNode.get("email").asText(), "yoga@studio.com");
		assertEquals(jsonNode.get("id").asInt(), 1);
	}

	@Test
	public void testUserDeleteUnauthorized_fail() throws Exception {
		// GIVEN WHEN THEN
		mockMvc.perform(delete("/api/user/99999")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testUserDeleteNotFound_fail() throws Exception {
		// GIVEN WHEN THEN
		mockMvc.perform(delete("/api/user/99999")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testUserDelete_success() throws Exception {
		// GIVEN
		String json1 = "{\"lastName\": \"toto\", \"firstName\": \"test!123\", \"email\": \"toto4@toto.com\", \"password\": \"test!1234\"}";
		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(json1))
				.andExpect(status().isOk());
		String json2 = "{\"email\": \"toto4@toto.com\", \"password\": \"test!1234\"}";
		MvcResult result = mockMvc
				.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(json2))
				.andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
		int id = jsonNode.get("id").asInt();
		// WHEN THEN
		mockMvc.perform(delete("/api/user/" + id)).andExpect(status().isOk());
		mockMvc.perform(get("/api/user/" + id)).andExpect(status().isNotFound());
	}

}
