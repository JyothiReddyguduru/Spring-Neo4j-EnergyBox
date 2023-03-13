package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.GatewaySensorModel;
import com.energybox.backendcodingchallenge.service.GatewayService;
import com.energybox.backendcodingchallenge.service.SensorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import java.io.IOException;

@RestController
@RequestMapping( value = "/gateways" )
public class GatewayController {

    private final GatewayService service;

    private final SensorService sensorService;

    public GatewayController( GatewayService service, SensorService sensorService ) {
        this.service = service;
        this.sensorService = sensorService;
    }

    @ApiOperation( value = "create a gateway", response = Gateway.class )
    @RequestMapping( value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> create(
            @RequestBody Gateway gateway 
    ) throws IOException, InterruptedException {
        service.createOrPutGateway(gateway);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation( value = "list all gateways", response = Gateway.class, responseContainer = "List" )
    @GetMapping( value = "")
    public ResponseEntity<List<Gateway>> getAll(@RequestParam(required = false) String type){
        return new ResponseEntity<>(service
                .getAllGateways(Optional.ofNullable(type).map(t-> SensorType.valueOf(type))
                .orElseGet(() -> null)), HttpStatus.OK);
    }

    @ApiOperation( value = "list one gateway by id", response = Gateway.class )
    @RequestMapping( value = "/{id}", method = RequestMethod.GET )
    public ResponseEntity<Gateway> getById(@PathVariable(value="id") Long id){
        return Optional.of(service.getGatewayById(id))
                       .map(gatewayOpt -> new ResponseEntity<Gateway>(gatewayOpt.get(), 
                            HttpStatus.OK))
                       .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation( value = "add sensor to a gateway", response = ResponseEntity.class )
    @PostMapping(value = "/update")
    public ResponseEntity<Object> mapGatewayToSensor(@RequestBody GatewaySensorModel gatewaySensorModel) {
        Optional<Sensor> sensor = sensorService.getSensorById(gatewaySensorModel.getSensorId());
                
        sensor.ifPresent(s -> {
            Optional<Gateway> gateway = service.getGatewayById(gatewaySensorModel.getGatewayId());
            gateway.ifPresent(g -> {
                if(g.getSensors().isEmpty()) {
                    Set<Sensor> sensors = new HashSet<>();
                    sensors.add(s);
                    g.setSensors(sensors);
                } else {
                    g.getSensors().add(s);
                    g.setSensors(g.getSensors());
                }
                service.createOrPutGateway(g);
            });
        });
        return new ResponseEntity<>(HttpStatus.OK );
    }
    

}
