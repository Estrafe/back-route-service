package me.diegxherrera.backrouteservice.event.impl;

import lombok.RequiredArgsConstructor;
import me.diegxherrera.backrouteservice.dto.event.RouteCreatedEventDTO;
import me.diegxherrera.backrouteservice.dto.event.RouteDeletedEventDTO;
import me.diegxherrera.backrouteservice.dto.event.RouteUpdatedEventDTO;
import me.diegxherrera.backrouteservice.event.EventPublisher;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventPublisherImpl implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange routeExchange; // The exchange where the events will be sent

    @Override
    public void publishRouteCreatedEvent(RouteCreatedEventDTO event) {
        try {
            rabbitTemplate.convertAndSend(routeExchange.getName(), "route.created", event);
            System.out.println("Published Route Created Event: " + event);
        } catch (Exception e) {
            System.err.println("Error while publishing Route Created Event: " + e.getMessage());
            e.printStackTrace();  // Stack trace for further investigation
        }
    }

    @Override
    public void publishRouteUpdatedEvent(RouteUpdatedEventDTO event) {
        rabbitTemplate.convertAndSend(routeExchange.getName(), "route.updated", event);
        System.out.println("Published Route Updated Event: " + event);
    }

    @Override
    public void publishRouteDeletedEvent(RouteDeletedEventDTO event) {
        rabbitTemplate.convertAndSend(routeExchange.getName(), "route.deleted", event);
        System.out.println("Published Route Deleted Event: " + event);
    }
}