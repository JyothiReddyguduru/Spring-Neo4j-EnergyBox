package com.energybox.backendcodingchallenge.controller;

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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Optional;
import java.io.IOException;

import com.energybox.backendcodingchallenge.custom.exception.EntityNotFoundException;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.GatewaySensorModel;
import com.energybox.backendcodingchallenge.service.GatewayService;

@RestController
@RequestMapping(value = "/gateways")
@ApiOperation("Gateways API")
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService service) {
        this.gatewayService = service;
    }

    @ApiOperation(value = "list all gateways", response = Gateway.class, responseContainer = "List")
    @GetMapping(value = "")
    public ResponseEntity<List<Gateway>> getAll(@RequestParam(required = false) String type) {
        return new ResponseEntity<>(gatewayService
                .getAllGateways(Optional.ofNullable(type).map(t -> SensorType.valueOf(type))
                        .orElseGet(() -> null)),
                HttpStatus.OK);
    }

    @ApiOperation(value = "list one gateway by id", response = Gateway.class)
    @ApiResponse(code = 200, message = "Gateway found")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Gateway> getById(@PathVariable(value = "id") Long id) {
        return Optional.of(gatewayService.getGatewayById(id))
                .map(gatewayOpt -> new ResponseEntity<Gateway>(gatewayOpt.get(),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "create a gateway", response = Gateway.class)
    @ApiResponse(code = 200, message = "Successfully created")
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Gateway> create(
            @RequestBody Gateway gateway) throws IOException, InterruptedException {
        return new ResponseEntity<>(gatewayService.createOrPutGateway(gateway, false), HttpStatus.CREATED);
    }

    @ApiOperation(value = "update gateway with a sensor", response = ResponseEntity.class, notes = "Maps a sensor to given gateway if both exists")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully mapped"),
            @ApiResponse(code = 404, message = "Not found - Either gateway or sensor not found")
    })
    @PostMapping(value = "/update")
    public ResponseEntity<Gateway> mapGatewayToSensor(@RequestBody GatewaySensorModel gatewaySensorModel) {
        Gateway gateway = null;
        try {
            gateway = gatewayService.mapSensorToGatewayById(gatewaySensorModel.getSensorId(),
                    gatewaySensorModel.getGatewayId());
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gateway, HttpStatus.OK);
    }

}
