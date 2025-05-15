package me.diegxherrera.backrouteservice.dto.event;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RouteUpdatedEventDTO implements Serializable {

    private UUID routeId;
    private String routeName;
    private List<UUID> stationIds;
    private boolean active;
    private String description;
    private String routeType;
}