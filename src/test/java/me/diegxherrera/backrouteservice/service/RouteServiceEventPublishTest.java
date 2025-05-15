package me.diegxherrera.backrouteservice.service;

import me.diegxherrera.backrouteservice.dto.event.RouteCreatedEventDTO;
import me.diegxherrera.backrouteservice.dto.event.RouteDeletedEventDTO;
import me.diegxherrera.backrouteservice.dto.event.RouteUpdatedEventDTO;
import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.request.UpdateRouteRequestDTO;
import me.diegxherrera.backrouteservice.event.EventPublisher;
import me.diegxherrera.backrouteservice.mapper.RouteMapper;
import me.diegxherrera.backrouteservice.model.RouteTypeEnum;
import me.diegxherrera.backrouteservice.repository.RouteRepository;
import me.diegxherrera.backrouteservice.service.impl.RouteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RouteServiceEventPublishTest {

    private RouteServiceImpl routeService;
    private EventPublisher eventPublisher;
    private RouteRepository routeRepository;
    private RouteMapper routeMapper;

    @BeforeEach
    void setUp() {
        routeRepository = mock(RouteRepository.class);
        eventPublisher = mock(EventPublisher.class);
        routeMapper = mock(RouteMapper.class);
        routeService = new RouteServiceImpl(routeRepository, routeMapper, eventPublisher);
    }

    @Test
    void testPublishRoutCreatedEvent() {
        CreateRouteRequestDTO request = CreateRouteRequestDTO.builder()
                .routeName("Event Test Route")
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .active(true)
                .description("desc")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        // Mocked RouteEntity to simulate the mapper output
        var routeEntity = requestToEntity(request);

        // Mock mapper and repository behavior
        when(routeMapper.fromCreateRequest(any())).thenReturn(routeEntity);
        when(routeRepository.save(any())).thenReturn(routeEntity);

        // Run service logic
        routeService.createRoute(request);

        // Capture the published event
        ArgumentCaptor<RouteCreatedEventDTO> captor = ArgumentCaptor.forClass(RouteCreatedEventDTO.class);
        verify(eventPublisher, times(1)).publishRouteCreatedEvent(captor.capture());

        // Assert event was published with correct data
        assertThat(captor.getValue().getRouteName()).isEqualTo("Event Test Route");
    }

    @Test
    void testPublishRouteUpdatedEvent() {
        UUID id = UUID.randomUUID();
        var original = requestToEntity(CreateRouteRequestDTO.builder()
                .routeName("Original")
                .stationIds(List.of(UUID.randomUUID()))
                .active(true)
                .description("Old")
                .routeType(RouteTypeEnum.REGIONAL)
                .build());
        original.setId(id);

        when(routeRepository.findById(id)).thenReturn(java.util.Optional.of(original));
        when(routeRepository.save(any())).thenReturn(original);

        UpdateRouteRequestDTO update = UpdateRouteRequestDTO.builder()
                .routeName("Updated")
                .description("Updated Desc")
                .active(false)
                .build();

        routeService.updateRoute(id, update);

        verify(eventPublisher, times(1)).publishRouteUpdatedEvent(any(RouteUpdatedEventDTO.class));
    }

    @Test
    void testPublishRouteDeletedEvent() {
        UUID id = UUID.randomUUID();
        var route = requestToEntity(CreateRouteRequestDTO.builder()
                .routeName("Delete Test")
                .stationIds(List.of(UUID.randomUUID()))
                .active(true)
                .description("desc")
                .routeType(RouteTypeEnum.REGIONAL)
                .build());
        route.setId(id);

        when(routeRepository.findById(id)).thenReturn(java.util.Optional.of(route));

        routeService.deleteRoute(id);

        ArgumentCaptor<RouteDeletedEventDTO> captor = ArgumentCaptor.forClass(RouteDeletedEventDTO.class);
        verify(eventPublisher).publishRouteDeletedEvent(captor.capture());
        assertThat(captor.getValue().getRouteName()).isEqualTo("Delete Test");
    }

    // Helper to mimic mapper behavior
    private me.diegxherrera.backrouteservice.model.RouteEntity requestToEntity(CreateRouteRequestDTO dto) {
        return me.diegxherrera.backrouteservice.model.RouteEntity.builder()
                .routeName(dto.getRouteName())
                .stationIds(dto.getStationIds())
                .active(dto.getActive())
                .description(dto.getDescription())
                .routeType(dto.getRouteType())
                .build();
    }
}