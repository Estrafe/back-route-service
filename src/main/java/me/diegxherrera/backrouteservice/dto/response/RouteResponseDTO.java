package me.diegxherrera.backrouteservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.diegxherrera.backrouteservice.model.RouteTypeEnum;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteResponseDTO {
    private UUID id;
    private String routeName;
    private List<UUID> stationIds;
    private boolean active;
    private String description;
    private RouteTypeEnum routeType;
}