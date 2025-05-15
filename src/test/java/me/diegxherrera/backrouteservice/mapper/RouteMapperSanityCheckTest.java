package me.diegxherrera.backrouteservice.mapper;

import me.diegxherrera.backrouteservice.dto.request.CreateRouteRequestDTO;
import me.diegxherrera.backrouteservice.model.RouteEntity;
import me.diegxherrera.backrouteservice.model.RouteTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RouteMapperSanityCheckTest {

    @Autowired
    private RouteMapper mapper;

    @Test
    void sanityCheckFromDTO() {
        CreateRouteRequestDTO dto = CreateRouteRequestDTO.builder()
                .routeName("Test")
                .stationIds(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .active(true)
                .description("Check")
                .routeType(RouteTypeEnum.REGIONAL)
                .build();

        RouteEntity entity = mapper.fromCreateRequest(dto);
        System.out.println("Mapped entity.routeType = " + entity.getRouteType());
        assertNotNull(entity.getRouteType());
    }
}