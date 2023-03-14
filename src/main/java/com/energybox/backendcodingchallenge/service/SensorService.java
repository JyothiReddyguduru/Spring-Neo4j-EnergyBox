package com.energybox.backendcodingchallenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
import java.util.Optional;

import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorReadingModel;
import com.energybox.backendcodingchallenge.domain.SensorReading;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.custom.exception.EntityNotFoundException;
import com.energybox.backendcodingchallenge.custom.exception.DuplicateEntityFoundException;

/**
 * Service entry point for sensor object manipulations
 */
@Service
public class SensorService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SensorService.class);

    private final SensorReadingService readingService;

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository, SensorReadingService readingService) {
        this.sensorRepository = sensorRepository;
        this.readingService = readingService;
    }

     /***
     * Return list of all sensors
     * @return List<Sensor>
     */
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    /***
     * Return list of all sensors that belong to a sensor type
     * @param sensorType
     * @return sensor
     */
    public List<Sensor> getAllSensorsByType(SensorType sensorType) {
        return sensorRepository.findAllSensorsBySensorType(sensorType);
    }

    /**
     * Returns an optional of sensor having given id
     * @param id
     * @return Optional<Sensor>
     */
    public Optional<Sensor> getSensorById(Long id) {
        return sensorRepository.findById(id);
    }

    /***
     * Create a new sensor if not present else update
     * @param sensor
     * @param isUpdate
     * @return
     */
    public Sensor createOrPutSensor(Sensor sensor, boolean isUpdate) {
        // check if gateway is already present for creation;
        Optional<Sensor> optional = sensorRepository.findOneBySensorId(sensor.getSensorId());
        // new gateway creation and if it is not a duplicate
        if (optional.isPresent() && !isUpdate) {
            LOGGER.error("Duplicate Sensor object found with sensor id " + sensor.getSensorId() );
            throw new DuplicateEntityFoundException("Duplicate Sensor object with sensor id " + sensor.getSensorId());
        }
        sensor = sensorRepository.save(sensor);
        LOGGER.info(isUpdate ? "Sensor object updated": "Sensor object created");
        return sensor;
    }

    /**
     * Update last reading of a sensor for the given sensor type
     * if sensor and reading exists else new reading is added to sensor
     * 
     * @param readingModel
     * @return Sensor
     */
    public Sensor updateSensorReading(SensorReadingModel readingModel) {
        Optional<Sensor> sensor = getSensorById(readingModel.getSensorId());
        SensorType type = SensorType.valueOf(
                readingModel.getSensorType());
        if (sensor.isPresent()) {
            Sensor updatedSensor = sensor.get();
            updatedSensor.getReadings().stream()
                    .filter(t -> t.getSensorType().equals(type)).findAny().ifPresentOrElse(b -> {
                        b.setValue(readingModel.getValue());
                        b.setLastReadDate(new Date());
                        LOGGER.info("Update reading for " + readingModel.getSensorId() + "and" + type);
                        readingService.updateReading(b);
                    }, () -> {
                        SensorReading newReading = new SensorReading(new Date(), type, readingModel.getValue());
                        updatedSensor.getReadings().add(newReading);
                        LOGGER.info("Create new reading for " + readingModel.getSensorId());
                        LOGGER.info("Update sensor with new reading" + readingModel.getSensorId());
                        sensorRepository.save(updatedSensor);
                    });

            return getSensorById(readingModel.getSensorId()).get();
        }
        throw new EntityNotFoundException(Sensor.class.getName(), readingModel.getSensorId());
    }

}
