package me.diegxherrera.backrouteservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import me.diegxherrera.backrouteservice.model.RouteTypeEnum;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateRouteRequestDTO {

    @NotBlank(message = "Route name cannot be blank")
    private String routeName;

    @NotEmpty(message = "Station IDs must not be empty")
    private List<UUID> stationIds;

    @NotNull(message = "Active must be provided")
    private Boolean active;

    private String description;

    @NotNull(message = "Route type must be provided")
    private RouteTypeEnum routeType;
}