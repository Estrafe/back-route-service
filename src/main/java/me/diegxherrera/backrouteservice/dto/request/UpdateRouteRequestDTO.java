package me.diegxherrera.backrouteservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.diegxherrera.backrouteservice.model.RouteTypeEnum;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRouteRequestDTO {

    private String routeName;
    private List<UUID> stationIds; // List of station IDs in this route
    private boolean active;
    private String description; // Optional description
    private RouteTypeEnum routeType; // Optional route type
}