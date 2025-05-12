package me.diegxherrera.backrouteservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteUpdatedEventDTO {

    private UUID routeId;
    private String routeName;
    private List<UUID> stationIds;
    private boolean active;
    private String description;
    private String routeType;
}