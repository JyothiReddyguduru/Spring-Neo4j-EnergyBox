package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.domain.Gateway;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;

import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;
import java.util.Optional;

public interface GatewayRepository extends Neo4jRepository<Gateway, Long> {

    @Query("MATCH (n:Gateway)<-[:CONNECTED_TO]-(s:Sensor) WHERE any(sensorType IN s.sensorTypes WHERE sensorType =~ $type) RETURN n")
    List<Gateway> findAllBySensorType(SensorType type);

    Optional<Gateway> findOneByGatewayId(String gatewayId);

}
