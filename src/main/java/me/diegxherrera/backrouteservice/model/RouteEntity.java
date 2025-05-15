package me.diegxherrera.backrouteservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "route")
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @NotNull
    private String routeName;

    @ElementCollection
    @CollectionTable(name = "route_stations", joinColumns = @JoinColumn(name = "route_id"))
    @OrderColumn(name = "position")
    @Column(name = "station_id", nullable = false)
    @NotEmpty
    private List<UUID> stationIds;

    @Column(nullable = false)
    @NotNull
    private boolean active;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "route_type")
    @NotNull
    private RouteTypeEnum routeType;
}