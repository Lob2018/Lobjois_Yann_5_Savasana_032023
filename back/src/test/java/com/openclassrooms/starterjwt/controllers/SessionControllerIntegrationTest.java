package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
public class SessionControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();
	private JsonNode jsonNode;

	@BeforeEach
	public void init() throws JsonMappingException, JsonProcessingException {
		jsonNode = objectMapper.readTree("");
	}

	@Test
	public void testSessionFindAllUnauthorized_fail() throws Exception {
		// GIVEN WHEN
		MvcResult result = mockMvc.perform(get("/api/session")).andExpect(status().isUnauthorized()).andReturn();
		String jsonReturned = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(jsonReturned);
		// THEN
		assertEquals(jsonNode.get("message").asText(), "Full authentication is required to access this resource");
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionFindByIdNotFound_fail() throws Exception {
		// GIVEN WHEN THEn
		mockMvc.perform(get("/api/session/99999")).andExpect(status().isNotFound());
	}

	@Test
	public void testSessionFindByIdUnauthorized_fail() throws Exception {
		// GIVEN WHEN
		MvcResult result = mockMvc.perform(get("/api/session/1")).andExpect(status().isUnauthorized()).andReturn();
		String jsonReturned = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(jsonReturned);
		// THEN
		assertEquals(jsonNode.get("message").asText(), "Full authentication is required to access this resource");
	}

	@Test
	public void testSessionCreateUnauthorized_fail() throws Exception {
		// GIVEN
		String json = "{\"name\": \"session 1\", \"date\": \"2012-01-01\", \"teacher_id\": 5, \"users\": null, \"description\": \"my description\"}";
		// WHEN
		MvcResult result = mockMvc.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isUnauthorized()).andReturn();
		String jsonReturned = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(jsonReturned);
		// THEN
		assertEquals(jsonNode.get("message").asText(), "Full authentication is required to access this resource");
	}

	@Test
	public void testSessionUpdateUnauthorized_fail() throws Exception {
		// GIVEN
		String json = "{\"name\": \"session 1\", \"date\": \"2012-01-01T00:00:00.000+00:00\", \"teacher_id\": 1, \"users\": [], \"description\": \"my description\"}";
		// WHEN
		MvcResult result = mockMvc.perform(put("/api/session/2").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isUnauthorized()).andReturn();
		String jsonReturned = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(jsonReturned);
		// THEN
		assertEquals(jsonNode.get("message").asText(), "Full authentication is required to access this resource");
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionUpsertWithUnknownId_success() throws Exception {
		// GIVEN
		String json = "{\"name\": \"session 99999\", \"date\": \"2012-01-01T00:00:00.000+00:00\", \"teacher_id\": 1, \"users\": [], \"description\": \"my description\"}";
		// WHEN THEN
		MvcResult result = mockMvc
				.perform(put("/api/session/99999").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
		assertEquals(jsonNode.get("name").asText(), "session 99999");
		assertNotNull(jsonNode.get("id"));
	}

	@Test
	public void testSessionDeleteUnauthorized_fail() throws Exception {
		// GIVEN WHEN
		MvcResult result = mockMvc.perform(delete("/api/session/2")).andExpect(status().isUnauthorized()).andReturn();
		String jsonReturned = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(jsonReturned);
		// THEN
		assertEquals(jsonNode.get("message").asText(), "Full authentication is required to access this resource");
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionDeleteNotFound_fail() throws Exception {
		// GIVEN WHEN THEN
		mockMvc.perform(delete("/api/session/99999")).andExpect(status().isNotFound());
	}

	@Test
	public void testSessionParticipateUnauthorized_fail() throws Exception {
		// GIVEN WHEN
		MvcResult result = mockMvc.perform(post("/api/session/1/participate/2")).andExpect(status().isUnauthorized())
				.andReturn();
		String jsonReturned = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(jsonReturned);
		// THEN
		assertEquals(jsonNode.get("message").asText(), "Full authentication is required to access this resource");
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionParticipateNotFound_fail() throws Exception {
		// GIVEN WHEN THEN
		mockMvc.perform(post("/api/session/99999/participate/99999")).andExpect(status().isNotFound());
	}

	@Test
	public void testSessionNoLongerParticipateUnauthorized_fail() throws Exception {
		// GIVEN WHEN
		MvcResult result = mockMvc.perform(delete("/api/session/1/participate/2")).andExpect(status().isUnauthorized())
				.andReturn();
		String jsonReturned = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(jsonReturned);
		// THEN
		assertEquals(jsonNode.get("message").asText(), "Full authentication is required to access this resource");
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionNoLongerParticipateNotFound_fail() throws Exception {
		// GIVEN WHEN THEN
		mockMvc.perform(delete("/api/session/99999/participate/99999")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionCreate_success() throws Exception {
		// GIVEN
		String json = "{\"name\": \"session 0\", \"date\": \"2012-01-01\", \"teacher_id\": 2, \"users\": null, \"description\": \"my description\"}";
		// WHEN
		MvcResult result = mockMvc.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn();
		String jsonReturned = result.getResponse().getContentAsString();
		jsonNode = objectMapper.readTree(jsonReturned);
		// THEN
		assertEquals(jsonNode.get("name").asText(), "session 0");
		assertNotNull(jsonNode.get("id"));
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionFindAll_success() throws Exception {
		// GIVEN
		String session1 = "{\"name\": \"session 1\", \"date\": \"2012-01-01\", \"teacher_id\": 2, \"users\": null, \"description\": \"my description\"}";
		String session2 = "{\"name\": \"session 2\", \"date\": \"2012-01-01\", \"teacher_id\": 2, \"users\": null, \"description\": \"my description\"}";
		mockMvc.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON).content(session1))
				.andExpect(status().isOk());
		mockMvc.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON).content(session2))
				.andExpect(status().isOk());
		// WHEN THEN
		mockMvc.perform(get("/api/session")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name", is("session 1"))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[1].name", is("session 2"))).andExpect(jsonPath("$[1].id", is(2)));
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionFindById_success() throws Exception {
		// GIVEN
		String session1 = "{\"name\": \"session 10\", \"date\": \"2012-01-01\", \"teacher_id\": 2, \"users\": null, \"description\": \"my description\"}";
		MvcResult result1 = mockMvc
				.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON).content(session1))
				.andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result1.getResponse().getContentAsString());
		final int id = jsonNode.get("id").asInt();
		// WHEN
		MvcResult result2 = mockMvc.perform(get("/api/session/" + id)).andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result2.getResponse().getContentAsString());
		// THEN
		assertEquals(jsonNode.get("name").asText(), "session 10");
		assertEquals(jsonNode.get("id").asInt(), id);
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionUpdate_success() throws Exception {
		// GIVEN
		String sessionUpdated = "{\"name\": \"session updated\", \"date\": \"2012-01-01T00:00:00.000+00:00\", \"teacher_id\": 1, \"users\": [], \"description\": \"my description\"}";
		String session1 = "{\"name\": \"session 10\", \"date\": \"2012-01-01\", \"teacher_id\": 2, \"users\": null, \"description\": \"my description\"}";
		MvcResult result1 = mockMvc
				.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON).content(session1))
				.andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result1.getResponse().getContentAsString());
		final int id = jsonNode.get("id").asInt();
		// WHEN
		MvcResult result = mockMvc
				.perform(put("/api/session/" + id).contentType(MediaType.APPLICATION_JSON).content(sessionUpdated))
				.andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
		// THEN
		assertEquals(jsonNode.get("name").asText(), "session updated");
		assertEquals(jsonNode.get("id").asInt(), id);
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionDelete_success() throws Exception {
		// GIVEN
		String session1 = "{\"name\": \"session 10\", \"date\": \"2012-01-01\", \"teacher_id\": 2, \"users\": null, \"description\": \"my description\"}";
		MvcResult result1 = mockMvc
				.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON).content(session1))
				.andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result1.getResponse().getContentAsString());
		int id = jsonNode.get("id").asInt();
		mockMvc.perform(get("/api/session/" + id)).andExpect(status().isOk());
		jsonNode = objectMapper.readTree(result1.getResponse().getContentAsString());
		id = jsonNode.get("id").asInt();
		// WHEN THEN
		mockMvc.perform(delete("/api/session/" + id)).andExpect(status().isOk());
		mockMvc.perform(get("/api/session/" + id)).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionParticipate_success() throws Exception {
		// GIVEN
		String session1 = "{\"name\": \"session 10\", \"date\": \"2012-01-01\", \"teacher_id\": 2, \"users\": null, \"description\": \"my description\"}";
		MvcResult result1 = mockMvc
				.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON).content(session1))
				.andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result1.getResponse().getContentAsString());
		int id = jsonNode.get("id").asInt();
		mockMvc.perform(get("/api/session/" + id)).andExpect(status().isOk());
		// WHEN THEN
		mockMvc.perform(post("/api/session/" + id + "/participate/" + 1)).andExpect(status().isOk()).andReturn();
	}

	@Test
	@WithMockUser(username = "yoga@studio.com")
	public void testSessionNoLongerParticipate_success() throws Exception {
		// GIVEN
		String session1 = "{\"name\": \"session 10\", \"date\": \"2012-01-01\", \"teacher_id\": 2, \"users\": null, \"description\": \"my description\"}";
		MvcResult result1 = mockMvc
				.perform(post("/api/session").contentType(MediaType.APPLICATION_JSON).content(session1))
				.andExpect(status().isOk()).andReturn();
		jsonNode = objectMapper.readTree(result1.getResponse().getContentAsString());
		int id = jsonNode.get("id").asInt();
		mockMvc.perform(get("/api/session/" + id)).andExpect(status().isOk());
		mockMvc.perform(post("/api/session/" + id + "/participate/" + 1)).andExpect(status().isOk()).andReturn();
		// WHEN THEN
		mockMvc.perform(delete("/api/session/" + id + "/participate/" + 1)).andExpect(status().isOk()).andReturn();
	}
}
