package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.repository.SensorReadingRepository;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.domain.SensorReading;
import com.energybox.backendcodingchallenge.domain.SensorReadingModel;

import java.util.Date;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SensorReadingService {

    private SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public Optional<SensorReading> getReadingByType(Long id, SensorType type) {
        return sensorReadingRepository.findByIdAndSensorType(id, type);
    }

    public Optional<SensorReading> getReadingById(Long id) {
        if(id == null){
            return Optional.empty();
        }
        return sensorReadingRepository.findById(id);
    }

    public SensorReading updateReading(SensorReadingModel reading) {
        SensorReading sensorReading = new SensorReading(new Date(), 
                            SensorType.valueOf(reading.getSensorType()), reading.getValue());
        return sensorReadingRepository.save(sensorReading);
    }
    
}
