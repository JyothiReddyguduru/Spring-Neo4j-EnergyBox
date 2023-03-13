package com.energybox.backendcodingchallenge.service;


import com.energybox.backendcodingchallenge.repository.SensorReadingRepository;
import com.energybox.backendcodingchallenge.domain.SensorReading;
import com.energybox.backendcodingchallenge.domain.SensorReadingModel;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class SensorReadingService {

    private SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public SensorReading updateReading(SensorReadingModel reading) {
        SensorReading sensorReading = new SensorReading(new Date(), reading.getValue());
        return sensorReadingRepository.save(sensorReading);
    }
    
}
