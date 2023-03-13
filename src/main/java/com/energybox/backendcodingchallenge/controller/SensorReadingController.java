package com.energybox.backendcodingchallenge.controller;


import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorReadingModel;
import com.energybox.backendcodingchallenge.domain.SensorReading;
import com.energybox.backendcodingchallenge.service.SensorReadingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping( value = "/sensorReadings" )
public class SensorReadingController {
    
    private final SensorReadingService service;

    public SensorReadingController(SensorReadingService service) {
        this.service = service;
    }

    @ApiOperation( value = "Update a sensor reading", response = Sensor.class )
    @RequestMapping( value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<SensorReading> update(
            @RequestBody SensorReadingModel reading
    ) throws IOException, InterruptedException {
        return new ResponseEntity<>( service.updateReading(reading),  HttpStatus.OK );
    }
}
