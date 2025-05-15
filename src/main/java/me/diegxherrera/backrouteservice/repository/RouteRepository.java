package me.diegxherrera.backrouteservice.repository;

import me.diegxherrera.backrouteservice.model.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, UUID> {

    Optional<RouteEntity> findByRouteName(String routeName);
    boolean existsByRouteName(String routeName);
}