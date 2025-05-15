package me.diegxherrera.backrouteservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class BackRouteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackRouteServiceApplication.class, args);
    }

}
