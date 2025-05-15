package me.diegxherrera.backrouteservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RouteServiceDuplicateTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RouteRepository routeRepository;

    private String duplicatedRouteName;

    @BeforeEach
    void setUp() throws Exception {
        routeRepository.deleteAll();

        duplicatedRouteName = "Duplicate Route";

        CreateRouteRequestDTO request = CreateRouteRequestDTO.builder()
                .routeName(duplicatedRouteName)
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .active(true)
                .description("Original route")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateRoute_WithSameName_ShouldFail() throws Exception {
        CreateRouteRequestDTO duplicateRequest = CreateRouteRequestDTO.builder()
                .routeName(duplicatedRouteName)
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .active(true)
                .description("Duplicate route")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict()); // Assuming 409 returned on duplicate
    }
}