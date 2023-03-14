package com.energybox.backendcodingchallenge.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.energybox.backendcodingchallenge.repository.SensorReadingRepository;
import com.energybox.backendcodingchallenge.domain.SensorReading;

@Service
public class SensorReadingService {

    private SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    /***
     * Return Sensor Reading by Id
     * @param id
     * @return SensorReading optional
     */
    public Optional<SensorReading> getReadingById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return sensorReadingRepository.findById(id);
    }

    /**
     * save reading of a sensor
     * @param reading
     * @return
     */
    public SensorReading updateReading(SensorReading reading) {
        return sensorReadingRepository.save(reading);
    }

}
