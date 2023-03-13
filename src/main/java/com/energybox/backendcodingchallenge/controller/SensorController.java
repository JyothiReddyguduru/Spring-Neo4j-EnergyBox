package com.energybox.backendcodingchallenge.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.io.IOException;
import java.util.Optional;

import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorReadingModel;
import com.energybox.backendcodingchallenge.service.SensorService;
import com.energybox.backendcodingchallenge.custom.exception.EntityNotFoundException;

@RestController
@RequestMapping(value = "/sensors")
@ApiOperation("Sensors API")
public class SensorController {

    private final SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @ApiOperation(value = "List all sensors", response = Sensor.class, responseContainer = "List")
    @GetMapping(value = "")
    public ResponseEntity<List<Sensor>> getSensors(
            @ApiParam(value = "Type of the sensor", required = false) @RequestParam(required = false) String type) {
        if (type == null) {
            return new ResponseEntity<>(service.getAllSensors(), HttpStatus.OK);
        }
        return new ResponseEntity<>(service.getAllSensorsByType(SensorType.valueOf(type)), HttpStatus.OK);
    }

    @ApiOperation(value = "create a sensor", response = Gateway.class)
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sensor> create(
            @RequestBody Sensor sensor) throws IOException, InterruptedException {
        return new ResponseEntity<>(service.createOrPutSensor(sensor, false), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a sensor reading", response = Sensor.class)
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sensor> update(
            @RequestBody SensorReadingModel readingModel) throws IOException, InterruptedException {
        try {
            return new ResponseEntity<>(service.updateSensorReading(readingModel), HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "List one sensor by id", response = Sensor.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Sensor> getById(
            @ApiParam(value = "id of the sensor", required = true) @PathVariable(value = "id") Long id) {
        Optional<Sensor> sensor = service.getSensorById(id);
        if (sensor.isPresent()) {
            return new ResponseEntity<>(sensor.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
