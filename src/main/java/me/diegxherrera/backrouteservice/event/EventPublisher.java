package me.diegxherrera.backrouteservice.event;

import me.diegxherrera.backrouteservice.dto.event.RouteCreatedEventDTO;
import me.diegxherrera.backrouteservice.dto.event.RouteDeletedEventDTO;
import me.diegxherrera.backrouteservice.dto.event.RouteUpdatedEventDTO;

public interface EventPublisher {

    void publishRouteCreatedEvent(RouteCreatedEventDTO event);
    void publishRouteUpdatedEvent(RouteUpdatedEventDTO event);
    void publishRouteDeletedEvent(RouteDeletedEventDTO event);
}