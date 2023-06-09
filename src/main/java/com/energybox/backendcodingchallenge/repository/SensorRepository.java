package com.energybox.backendcodingchallenge.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

import com.energybox.backendcodingchallenge.domain.Sensor;

public interface SensorRepository extends Neo4jRepository<Sensor, Long> {

   @Query("MATCH (p:Sensor) WHERE any(sensorType IN p.sensorTypes WHERE sensorType =~ $type) RETURN p")
   List<Sensor> findAllSensorsBySensorType(SensorType type);

   Optional<Sensor> findOneBySensorId(String sensorId);

}