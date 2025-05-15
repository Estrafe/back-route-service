package me.diegxherrera.backrouteservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteRouteRequestDTO {

    // Unique identifier of the route to be deleted
    @NotNull(message = "Route ID cannot be null")
    private UUID routeId;

    // Optional: Reason for deletion
    private String reason;
}