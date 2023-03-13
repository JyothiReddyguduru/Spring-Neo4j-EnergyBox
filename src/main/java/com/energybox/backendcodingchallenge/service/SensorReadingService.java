package com.energybox.backendcodingchallenge.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.energybox.backendcodingchallenge.repository.SensorReadingRepository;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.domain.SensorReading;

@Service
public class SensorReadingService {

    Logger LOGGER = LoggerFactory.getLogger(SensorReadingService.class);

    private SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public Optional<SensorReading> getReadingByType(Long id, SensorType type) {
        return sensorReadingRepository.findByIdAndSensorType(id, type);
    }

    public Optional<SensorReading> getReadingById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return sensorReadingRepository.findById(id);
    }

    public SensorReading updateReading(SensorReading reading) {
        return sensorReadingRepository.save(reading);
    }

}
