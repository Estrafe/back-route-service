package me.diegxherrera.backrouteservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Define the name of the queue, exchange, and routing key for events
    public static final String ROUTE_CREATED_QUEUE = "route.created.queue";
    public static final String ROUTE_UPDATED_QUEUE = "route.updated.queue";
    public static final String ROUTE_DELETED_QUEUE = "route.deleted.queue";

    public static final String ROUTE_EXCHANGE = "route.exchange";

    // Define the routing keys
    public static final String ROUTE_CREATED_ROUTING_KEY = "route.created";
    public static final String ROUTE_UPDATED_ROUTING_KEY = "route.updated";
    public static final String ROUTE_DELETED_ROUTING_KEY = "route.deleted";

    // Declare the Queue for Route Created event
    @Bean
    public Queue routeCreatedQueue() {
        return new Queue(ROUTE_CREATED_QUEUE);
    }

    // Declare the Queue for Route Updated event
    @Bean
    public Queue routeUpdatedQueue() {
        return new Queue(ROUTE_UPDATED_QUEUE);
    }

    // Declare the Queue for Route Deleted event
    @Bean
    public Queue routeDeletedQueue() {
        return new Queue(ROUTE_DELETED_QUEUE);
    }

    // Declare the Topic Exchange
    @Bean
    public TopicExchange routeExchange() {
        return new TopicExchange(ROUTE_EXCHANGE);
    }

    // Bind the queues to the exchange with the routing keys
    @Bean
    public Binding routeCreatedBinding(Queue routeCreatedQueue, TopicExchange routeExchange) {
        return BindingBuilder.bind(routeCreatedQueue).to(routeExchange).with(ROUTE_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding routeUpdatedBinding(Queue routeUpdatedQueue, TopicExchange routeExchange) {
        return BindingBuilder.bind(routeUpdatedQueue).to(routeExchange).with(ROUTE_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding routeDeletedBinding(Queue routeDeletedQueue, TopicExchange routeExchange) {
        return BindingBuilder.bind(routeDeletedQueue).to(routeExchange).with(ROUTE_DELETED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}