package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorReadingModel;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.custom.exception.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

    private final SensorReadingService readingService;

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository, SensorReadingService readingService) {
        this.sensorRepository = sensorRepository;
        this.readingService = readingService;
    }

    public Sensor createOrPutSensor(Sensor sensor){
        return sensorRepository.save(sensor);
    }

    public Sensor updateReading(SensorReadingModel reading) {
        Optional<Sensor> sensor = getSensorById(reading.getSensorId());
        if(sensor.isPresent()){
            Sensor updatedSensor = sensor.get();
            updatedSensor.setLastReading(readingService.updateReading(reading));
            sensorRepository.save(updatedSensor);
            return updatedSensor;
        }             
        throw new EntityNotFoundException(reading.getSensorId());
    }

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public List<Sensor> getAllSensorsByType(SensorType sensorType) {
        return sensorRepository.findAllSensorsBySensorType(sensorType);
    }

    public Optional<Sensor> getSensorById(Long id) { return sensorRepository.findById(id); }


}
