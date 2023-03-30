package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.security.test.context.support.WithMockUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TeacherControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();
	private JsonNode jsonNode;

	@BeforeEach
	public void init() throws JsonMappingException, JsonProcessingException {
		jsonNode = objectMapper.readTree("");
	}

	@Test
	public void testTeacherFindAllUnauthorized_fail() throws Exception {
		// GIVEN WHEN
		MvcResult result = mockMvc.perform(get("/api/teacher")).andExpect(status().isUnauthorized()).andReturn();
		jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
		// THEN
		assertEquals(jsonNode.get("message").asText(), "Full authentication is required to access this resource");
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testTeacherFindAll_success() throws Exception {
		// GIVEN WHEN THEN
		mockMvc.perform(get("/api/teacher")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].lastName", is("DELAHAYE"))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[1].lastName", is("THIERCELIN"))).andExpect(jsonPath("$[1].id", is(2)));
	}

	@Test
	public void testTeacherFindByIdUnauthorized_fail() throws Exception {
		// GIVEN WHEN
		MvcResult result = mockMvc.perform(get("/api/teacher/1")).andExpect(status().isUnauthorized()).andReturn();
		jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
		// THEN
		assertEquals(jsonNode.get("message").asText(), "Full authentication is required to access this resource");
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testTeacherFindByIdNotFound_fail() throws Exception {
		// GIVEN WHEN THEN
		mockMvc.perform(get("/api/teacher/99999")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testTeacherFindById_success() throws Exception {
		// GIVEN WHEN
		MvcResult result = mockMvc.perform(get("/api/teacher/1")).andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
		// THEN
		assertEquals(jsonNode.get("lastName").asText(), "DELAHAYE");
		assertEquals(jsonNode.get("id").asInt(), 1);
	}
}
