package me.diegxherrera.backrouteservice.event;

import me.diegxherrera.backrouteservice.dto.event.RouteCreatedEventDTO;
import me.diegxherrera.backrouteservice.event.impl.EventPublisherImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EventPublisherImplTest {

    private RabbitTemplate rabbitTemplate;
    private TopicExchange topicExchange;
    private EventPublisherImpl publisher;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        topicExchange = new TopicExchange("route.exchange");
        publisher = new EventPublisherImpl(rabbitTemplate, topicExchange);
    }

    @Test
    void testPublishRouteCreatedEvent() {
        RouteCreatedEventDTO event = new RouteCreatedEventDTO(
                UUID.randomUUID(), "Test Route", List.of(UUID.randomUUID(), UUID.randomUUID()),
                true, "Test", "REGIONAL"
        );

        publisher.publishRouteCreatedEvent(event);

        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);

        verify(rabbitTemplate, times(1)).convertAndSend(eq("route.exchange"), routingKeyCaptor.capture(), eventCaptor.capture());

        assertEquals("route.created", routingKeyCaptor.getValue());
        assertEquals(event, eventCaptor.getValue());
    }
}