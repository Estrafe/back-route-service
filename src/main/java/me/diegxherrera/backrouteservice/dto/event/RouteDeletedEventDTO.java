package me.diegxherrera.backrouteservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDeletedEventDTO implements Serializable {

    // Unique identifier of the route being deleted
    private UUID routeId;

    // Name of the route being deleted (optional)
    private String routeName;
}