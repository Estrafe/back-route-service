package me.diegxherrera.backrouteservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stop {

    @Column(name = "station_id", nullable = false)
    private UUID stationId;

    @Column(name = "stop_order", nullable = false)
    private int order;

}