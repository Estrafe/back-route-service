package me.diegxherrera.backrouteservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.diegxherrera.backrouteservice.dto.event.RouteCreatedEventDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RouteCreatedEventDTOTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID routeId = UUID.randomUUID();
        List<UUID> stationIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        RouteCreatedEventDTO dto = new RouteCreatedEventDTO(
                routeId,
                "Test Route",
                stationIds,
                true,
                "Test Description",
                "REGIONAL"
        );

        assertEquals(routeId, dto.getRouteId());
        assertEquals("Test Route", dto.getRouteName());
        assertEquals(stationIds, dto.getStationIds());
        assertTrue(dto.isActive());
        assertEquals("Test Description", dto.getDescription());
        assertEquals("REGIONAL", dto.getRouteType());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        UUID routeId = UUID.randomUUID();
        UUID stationId = UUID.randomUUID();

        RouteCreatedEventDTO dto = new RouteCreatedEventDTO();
        dto.setRouteId(routeId);
        dto.setRouteName("Route A");
        dto.setStationIds(List.of(stationId));
        dto.setActive(true);
        dto.setDescription("Desc");
        dto.setRouteType("REGIONAL");

        assertEquals(routeId, dto.getRouteId());
        assertEquals("Route A", dto.getRouteName());
        assertEquals(1, dto.getStationIds().size());
        assertTrue(dto.isActive());
        assertEquals("Desc", dto.getDescription());
        assertEquals("REGIONAL", dto.getRouteType());
    }

    @Test
    void testJsonSerialization() throws Exception {
        RouteCreatedEventDTO dto = new RouteCreatedEventDTO(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "Route Test",
                List.of(UUID.fromString("22222222-2222-2222-2222-222222222222")),
                true,
                "Desc",
                "REGIONAL"
        );

        String json = objectMapper.writeValueAsString(dto);
        assertTrue(json.contains("Route Test"));
        assertTrue(json.contains("11111111-1111-1111-1111-111111111111"));
        assertTrue(json.contains("REGIONAL"));
    }

    @Test
    void testJsonDeserialization() throws Exception {
        String json = """
            {
              "routeId":"11111111-1111-1111-1111-111111111111",
              "routeName":"Route Test",
              "stationIds":["22222222-2222-2222-2222-222222222222"],
              "active":true,
              "description":"Desc",
              "routeType":"REGIONAL"
            }
        """;

        RouteCreatedEventDTO dto = objectMapper.readValue(json, RouteCreatedEventDTO.class);
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), dto.getRouteId());
        assertEquals("Route Test", dto.getRouteName());
        assertEquals(1, dto.getStationIds().size());
        assertTrue(dto.isActive());
    }
}