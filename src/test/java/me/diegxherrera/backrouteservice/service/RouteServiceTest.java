package me.diegxherrera.backrouteservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.request.UpdateRouteRequestDTO;
import me.diegxherrera.backrouteservice.model.RouteTypeEnum;
import me.diegxherrera.backrouteservice.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RouteServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RouteRepository routeRepository;

    private UUID existingRouteId;
    private String routeName;

    @BeforeEach
    void setUp() throws Exception {
        // Clean up to avoid duplicate name issues
        routeRepository.deleteAll();

        routeName = "Test Route " + UUID.randomUUID();

        CreateRouteRequestDTO createRequest = CreateRouteRequestDTO.builder()
                .routeName(routeName)
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .active(true)
                .description("Test Description")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        String response = mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.routeName", is(routeName)))
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        existingRouteId = UUID.fromString(jsonNode.get("id").asText());
    }

    @Test
    void testGetRouteById() throws Exception {
        mockMvc.perform(get("/routes/{id}", existingRouteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routeName", is(routeName)));
    }

    @Test
    void testUpdateRoute() throws Exception {
        // Make sure the `id` is excluded from the body, as it should be passed in the URL
        UpdateRouteRequestDTO updateRequest = UpdateRouteRequestDTO.builder()
                .routeName("Updated Route")
                .description("Updated Description")
                .active(false)
                .routeType(RouteTypeEnum.REGIONAL) // Ensure routeType is included
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID()))  // Ensure stationIds is included
                .build();

        // Perform the update request with the `id` in the URL
        mockMvc.perform(put("/routes/{id}", existingRouteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routeName", is("Updated Route")))
                .andExpect(jsonPath("$.active", is(false)))
                .andExpect(jsonPath("$.routeType", is("REGIONAL")));  // Add routeType check
    }

    @Test
    void testGetAllRoutes() throws Exception {
        mockMvc.perform(get("/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void testDeleteRoute() throws Exception {
        mockMvc.perform(delete("/routes/{id}", existingRouteId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/routes/{id}", existingRouteId))
                .andExpect(status().isNotFound());
    }
}