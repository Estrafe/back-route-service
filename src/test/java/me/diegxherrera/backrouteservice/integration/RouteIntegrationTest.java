package me.diegxherrera.backrouteservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.request.UpdateRouteRequestDTO;
import me.diegxherrera.backrouteservice.model.RouteTypeEnum;
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
public class RouteIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void fullRouteLifecycle() throws Exception {
        // Create
        CreateRouteRequestDTO request = CreateRouteRequestDTO.builder()
                .routeName("Integration Route")
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .active(true)
                .description("Lifecycle test")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        String content = objectMapper.writeValueAsString(request);
        String response = mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.routeName", is("Integration Route")))
                .andReturn().getResponse().getContentAsString();

        UUID routeId = UUID.fromString(objectMapper.readTree(response).get("id").asText());

        // Update
        UpdateRouteRequestDTO update = UpdateRouteRequestDTO.builder()
                .id(routeId) // ✅ Include the route ID
                .routeName("Updated Integration Route")
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID())) // ✅ Required
                .routeType(RouteTypeEnum.REGIONAL) // ✅ Required
                .active(false)
                .description("Updated")
                .build();

        mockMvc.perform(put("/routes/{id}", routeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routeName", is("Updated Integration Route")));

        // Get
        mockMvc.perform(get("/routes/{id}", routeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Updated")));

        // Delete
        mockMvc.perform(delete("/routes/{id}", routeId))
                .andExpect(status().isNoContent());

        // Confirm not found
        mockMvc.perform(get("/routes/{id}", routeId))
                .andExpect(status().isNotFound());
    }
}