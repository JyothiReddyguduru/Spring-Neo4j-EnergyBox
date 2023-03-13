package com.energybox.backendcodingchallenge.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import com.energybox.backendcodingchallenge.domain.SensorReading;

@Repository
public interface SensorReadingRepository extends Neo4jRepository<SensorReading, Long> {
    
}
