package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorReadingModel;
import com.energybox.backendcodingchallenge.service.SensorService;
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


@RestController
@RequestMapping( value = "/sensors" )
public class SensorController {

    private final SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @ApiOperation( value = "List all sensors", response = Sensor.class, responseContainer = "List")
    @GetMapping( value = "" )
    public ResponseEntity<List<Sensor>> getSensors( @ApiParam(value = "Type of the sensor", required = false)
    @RequestParam(required = false) String type){
        if(type == null){
            return new ResponseEntity<>(service.getAllSensors(), HttpStatus.OK);
        }
        return new ResponseEntity<>(service.getAllSensorsByType(SensorType.valueOf(type)), HttpStatus.OK);
    }

    @ApiOperation( value = "create a sensor", response = Gateway.class )
    @PostMapping( value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> create(
            @RequestBody Sensor sensor
    ) throws IOException, InterruptedException {
        service.createOrPutSensor(sensor);
        return new ResponseEntity<>( HttpStatus.OK );
    }

    @ApiOperation( value = "Update a sensor reading", response = Sensor.class )
    @RequestMapping( value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Sensor> update(
            @RequestBody SensorReadingModel reading
    ) throws IOException, InterruptedException {
        return new ResponseEntity<>( service.updateReading(reading),  HttpStatus.OK );
    }

    @ApiOperation( value = "List one sensor by id", response = Sensor.class )
    @RequestMapping( value = "/{id}", method = RequestMethod.GET )
    public ResponseEntity<Sensor> getById(@ApiParam(value = "id of the sensor", required = true)
        @PathVariable(value="id") Long id){
        Optional<Sensor> sensor = service.getSensorById(id);
        if(sensor.isPresent()){
            return new ResponseEntity<>(sensor.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
