package me.diegxherrera.backrouteservice.controller;

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
class RouteControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RouteRepository routeRepository;

    private UUID routeId;
    private String routeName;

    @BeforeEach
    void setUp() throws Exception {
        routeRepository.deleteAll();

        routeName = "Controller Test Route " + UUID.randomUUID();
        CreateRouteRequestDTO createDto = CreateRouteRequestDTO.builder()
                .routeName(routeName)
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .active(true)
                .description("Controller layer test")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        String response = mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.routeName", is(routeName)))
                .andReturn().getResponse().getContentAsString();

        routeId = UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    @Test
    void testGetRouteById_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/routes/{id}", routeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(routeId.toString())))
                .andExpect(jsonPath("$.routeName", is(routeName)));
    }

    @Test
    void testGetRouteByInvalidId_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/routes/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateRoute_ShouldReturnUpdatedValues() throws Exception {
        UpdateRouteRequestDTO updateDto = UpdateRouteRequestDTO.builder()
                .id(routeId)
                .routeName("Updated Controller Route")
                .description("Updated via controller test")
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .routeType(RouteTypeEnum.REGIONAL)
                .active(false)
                .build();

        mockMvc.perform(put("/routes/{id}", routeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routeName", is("Updated Controller Route")))
                .andExpect(jsonPath("$.active", is(false)));
    }

    @Test
    void testDeleteRoute_ShouldReturn204AndThen404() throws Exception {
        mockMvc.perform(delete("/routes/{id}", routeId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/routes/{id}", routeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllRoutes_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void testCreateRoute_InvalidBody_ShouldReturn400() throws Exception {
        // Missing required fields like routeName and stationIds
        String invalidPayload = "{}";

        mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateRoute_MissingRouteType_ShouldReturn400() throws Exception {
        String invalidPayload = "{"
                + "\"routeName\": \"Controller Test Route\","
                + "\"stationIds\": [\"9cd94ff4-0da6-4048-902b-cf954a71605f\",\"67e38e99-f958-4614-9913-98746c14a92a\"],"
                + "\"active\": true,"
                + "\"description\": \"Controller layer test\""
                + "}";

        mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.routeType", is("Route type must be provided"))); // <- update here
    }
}