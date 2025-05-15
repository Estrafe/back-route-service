package me.diegxherrera.backrouteservice.service.impl;

import lombok.RequiredArgsConstructor;
import me.diegxherrera.backrouteservice.dto.event.RouteCreatedEventDTO;
import me.diegxherrera.backrouteservice.dto.event.RouteDeletedEventDTO;
import me.diegxherrera.backrouteservice.dto.event.RouteUpdatedEventDTO;
import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.request.UpdateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.response.RouteResponseDTO;
import me.diegxherrera.backrouteservice.event.EventPublisher;
import me.diegxherrera.backrouteservice.exception.RouteNotFoundException;
import me.diegxherrera.backrouteservice.mapper.RouteMapper;
import me.diegxherrera.backrouteservice.model.RouteEntity;
import me.diegxherrera.backrouteservice.repository.RouteRepository;
import me.diegxherrera.backrouteservice.service.RouteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final EventPublisher eventPublisher;  // Injecting Event Publisher

    @Override
    @Transactional
    public RouteResponseDTO createRoute(CreateRouteRequestDTO request) {
        RouteEntity routeEntity = routeMapper.fromCreateRequest(request);
        if (routeEntity.getRouteType() == null) {
            throw new IllegalArgumentException("Route type cannot be null.");
        }
        RouteEntity savedRoute = routeRepository.save(routeEntity);

        // Publish RouteCreatedEvent
        RouteCreatedEventDTO event = new RouteCreatedEventDTO(
                savedRoute.getId(), savedRoute.getRouteName(), savedRoute.getStationIds(),
                savedRoute.isActive(), savedRoute.getDescription(), savedRoute.getRouteType().toString()
        );
        eventPublisher.publishRouteCreatedEvent(event);

        return routeMapper.toResponseDTO(savedRoute);
    }

    @Override
    @Transactional
    public RouteResponseDTO updateRoute(UUID id, UpdateRouteRequestDTO request) {
        // Make sure 'id' is fetched from the URL path (this is handled in the controller)
        if (id == null) {
            throw new IllegalArgumentException("Route ID cannot be null");
        }

        // Fetch the route entity from the repository
        RouteEntity routeEntity = routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Route not found"));

        // Map the updated values from the DTO to the existing route entity
        routeMapper.partialUpdate(routeEntity, request);

        // Save the updated route entity
        RouteEntity updatedRoute = routeRepository.save(routeEntity);

        // Publish RouteUpdatedEvent
        RouteUpdatedEventDTO event = new RouteUpdatedEventDTO(
                updatedRoute.getId(), updatedRoute.getRouteName(), updatedRoute.getStationIds(),
                updatedRoute.isActive(), updatedRoute.getDescription(),
                updatedRoute.getRouteType() != null ? updatedRoute.getRouteType().toString() : null
        );
        eventPublisher.publishRouteUpdatedEvent(event);

        // Return the response DTO
        return routeMapper.toResponseDTO(updatedRoute);
    }

    @Override
    @Transactional
    public void deleteRoute(UUID id) {
        RouteEntity routeEntity = routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Route not found"));

        routeRepository.delete(routeEntity);

        // Publish RouteDeletedEvent
        RouteDeletedEventDTO event = new RouteDeletedEventDTO(id, routeEntity.getRouteName());
        eventPublisher.publishRouteDeletedEvent(event);
    }

    @Override
    public List<RouteResponseDTO> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(routeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RouteResponseDTO getRouteById(UUID id) {
        RouteEntity routeEntity = routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Route not found"));

        return routeMapper.toResponseDTO(routeEntity);
    }
}