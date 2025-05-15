package me.diegxherrera.backrouteservice.mapper;

import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.request.UpdateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.response.RouteResponseDTO;
import me.diegxherrera.backrouteservice.model.RouteEntity;
import me.diegxherrera.backrouteservice.model.RouteTypeEnum;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RouteMapperTest {

    private final RouteMapper mapper = Mappers.getMapper(RouteMapper.class);

    @Test
    void testFromCreateRequest() {
        CreateRouteRequestDTO request = CreateRouteRequestDTO.builder()
                .routeName("Route A")
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .active(true)
                .description("Test route")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        RouteEntity entity = mapper.fromCreateRequest(request);

        assertNotNull(entity);
        assertEquals("Route A", entity.getRouteName());
        assertEquals(2, entity.getStationIds().size());
        assertTrue(entity.isActive());
        assertEquals("Test route", entity.getDescription());
        assertEquals(RouteTypeEnum.REGIONAL, entity.getRouteType());
    }

    @Test
    void testPartialUpdate() {
        RouteEntity entity = RouteEntity.builder()
                .id(UUID.randomUUID())
                .routeName("Original")
                .stationIds(List.of(UUID.randomUUID()))
                .active(true)
                .description("Old description")
                .routeType(RouteTypeEnum.EXPRESS)
                .build();

        UpdateRouteRequestDTO update = UpdateRouteRequestDTO.builder()
                .routeName("Updated Name")
                .description("New description")
                .active(false)
                .build();

        mapper.partialUpdate(entity, update);

        assertEquals("Updated Name", entity.getRouteName());
        assertEquals("New description", entity.getDescription());
        assertFalse(entity.isActive());
        assertEquals(RouteTypeEnum.EXPRESS, entity.getRouteType()); // Unchanged
    }

    @Test
    void testToResponseDTO() {
        UUID routeId = UUID.randomUUID();
        List<UUID> stationIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        RouteEntity entity = RouteEntity.builder()
                .id(routeId)
                .routeName("Mapped Route")
                .stationIds(stationIds)
                .active(true)
                .description("Response mapping")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        RouteResponseDTO dto = mapper.toResponseDTO(entity);

        assertEquals(routeId, dto.getId());
        assertEquals("Mapped Route", dto.getRouteName());
        assertEquals(stationIds, dto.getStationIds());
        assertTrue(dto.isActive());
        assertEquals("Response mapping", dto.getDescription());
        assertEquals(RouteTypeEnum.REGIONAL, dto.getRouteType());
    }
}