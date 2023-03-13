package com.energybox.backendcodingchallenge.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import java.util.List;
import org.springframework.data.neo4j.repository.query.Query;

import com.energybox.backendcodingchallenge.domain.Sensor;

@Repository
public interface SensorRepository extends Neo4jRepository<Sensor, Long> {

   @Query("MATCH (p:Sensor) WHERE any(sensorType IN p.sensorTypes WHERE sensorType =~ $type) RETURN p")
   List<Sensor> findAllSensorsBySensorType(SensorType type);

}