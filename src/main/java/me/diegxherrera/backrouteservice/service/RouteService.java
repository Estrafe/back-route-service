package me.diegxherrera.backrouteservice.service;

import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.request.UpdateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.response.RouteResponseDTO;

import java.util.List;
import java.util.UUID;

public interface RouteService {
    RouteResponseDTO createRoute(CreateRouteRequestDTO request);
    RouteResponseDTO getRouteById(UUID id);
    List<RouteResponseDTO> getAllRoutes();
    RouteResponseDTO updateRoute(UUID id, UpdateRouteRequestDTO request);
    void deleteRoute(UUID id);
}