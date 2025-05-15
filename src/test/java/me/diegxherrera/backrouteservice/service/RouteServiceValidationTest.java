package me.diegxherrera.backrouteservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
import me.diegxherrera.backrouteservice.model.RouteTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RouteServiceValidationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void testCreateRoute_MissingRouteName_ShouldFail() throws Exception {
        CreateRouteRequestDTO request = CreateRouteRequestDTO.builder()
                .stationIds(java.util.List.of(UUID.randomUUID()))
                .active(true)
                .description("Missing name")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateRoute_EmptyStationList_ShouldFail() throws Exception {
        CreateRouteRequestDTO request = CreateRouteRequestDTO.builder()
                .routeName("Invalid Empty Stations")
                .stationIds(java.util.Collections.emptyList())
                .active(true)
                .description("Invalid")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateRoute_NullPayload_ShouldFail() throws Exception {
        mockMvc.perform(post("/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}