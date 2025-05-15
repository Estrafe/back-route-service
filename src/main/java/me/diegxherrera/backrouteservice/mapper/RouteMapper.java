package me.diegxherrera.backrouteservice.mapper;

import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.request.UpdateRouteRequestDTO;
import me.diegxherrera.backrouteservice.dto.response.RouteResponseDTO;
import me.diegxherrera.backrouteservice.model.RouteEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RouteMapper {

    // Mapping from CreateRouteRequestDTO to RouteEntity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "routeType", source = "routeType")
    RouteEntity fromCreateRequest(CreateRouteRequestDTO request);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "routeName", source = "routeName"),
            @Mapping(target = "stationIds", source = "stationIds"),
            @Mapping(target = "active", source = "active"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "routeType", source = "routeType")
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget RouteEntity existingRoute, UpdateRouteRequestDTO request);

    RouteResponseDTO toResponseDTO(RouteEntity routeEntity);
}