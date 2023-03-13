package com.energybox.backendcodingchallenge.repository;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import com.energybox.backendcodingchallenge.domain.SensorReading;

import java.util.Optional;

public interface SensorReadingRepository extends Neo4jRepository<SensorReading, Long> {
    
    Optional<SensorReading> findByIdAndSensorType(Long id, SensorType type);

}
