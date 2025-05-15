package me.diegxherrera.backrouteservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.request.UpdateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.response.RouteResponseDTO;
import me.diegxherrera.backrouteservice.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    // Endpoint for creating a new route
    @PostMapping
    public ResponseEntity<RouteResponseDTO> createRoute(@Valid @RequestBody CreateRouteRequestDTO request) {
        RouteResponseDTO response = routeService.createRoute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint for getting all routes
    @GetMapping
    public ResponseEntity<List<RouteResponseDTO>> getAllRoutes() {
        List<RouteResponseDTO> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(routes);
    }

    // Endpoint for getting a specific route by ID
    @GetMapping("/{id}")
    public ResponseEntity<RouteResponseDTO> getRouteById(@PathVariable UUID id) {
        RouteResponseDTO response = routeService.getRouteById(id);
        return ResponseEntity.ok(response);
    }

    // Endpoint for updating a specific route by ID
    @PutMapping("/{id}")
    public ResponseEntity<RouteResponseDTO> updateRoute(@PathVariable UUID id, @Valid @RequestBody UpdateRouteRequestDTO request) {
        RouteResponseDTO response = routeService.updateRoute(id, request);
        return ResponseEntity.ok(response);
    }

    // Endpoint for deleting a specific route by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable UUID id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}