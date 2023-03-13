package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorReadingModel;
import com.energybox.backendcodingchallenge.domain.SensorReading;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.custom.exception.EntityNotFoundException;
import com.energybox.backendcodingchallenge.custom.exception.DuplicateEntityFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
import java.util.Optional;

/**
 * Service entry point for sensor object manipulations
 */
@Service
public class SensorService {

    private final SensorReadingService readingService;

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository, SensorReadingService readingService) {
        this.sensorRepository = sensorRepository;
        this.readingService = readingService;
    }

    public Sensor createOrPutSensor(Sensor sensor, boolean isUpdate) {
        // check if gateway is already present for creation;
        Optional<Sensor> optional = sensorRepository.findOneBySensorId(sensor.getSensorId());
        // new gateway creation and if it is not a duplicate
        if (optional.isPresent() && !isUpdate) {
            throw new DuplicateEntityFoundException("Duplicate Sensor object");
        }
        return sensorRepository.save(sensor);
    }

    /**
     * Update last reading of a sensor for the given sensor type
     * if sensor exists
     * 
     * @param readingModel
     * @return
     */
    public Sensor updateSensorReading(SensorReadingModel readingModel) {
        Optional<Sensor> sensor = getSensorById(readingModel.getSensorId());
        if (sensor.isPresent()) {
            Sensor updatedSensor = sensor.get();
            SensorType type = SensorType.valueOf(
                    readingModel.getSensorType());
            // get reading by sensor id
            Optional<SensorReading> oldReading = readingService.getReadingById(readingModel.getReadingId());
            SensorReading newReading = null;
            if (oldReading.isPresent()) {
                newReading = oldReading.get();
                newReading.setValue(readingModel.getValue());
                newReading.setLastReadDate(new Date());
                readingService.updateReading(newReading);// save
            } else {
                newReading = new SensorReading(new Date(), type, readingModel.getValue());// create new reading
                updatedSensor.getReadings().add(newReading);
                sensorRepository.save(updatedSensor);
            }
            return getSensorById(readingModel.getSensorId()).get();
        }
        throw new EntityNotFoundException(SensorReading.class.getName(), readingModel.getSensorId());
    }

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public List<Sensor> getAllSensorsByType(SensorType sensorType) {
        return sensorRepository.findAllSensorsBySensorType(sensorType);
    }

    public Optional<Sensor> getSensorById(Long id) {
        return sensorRepository.findById(id);
    }

}
