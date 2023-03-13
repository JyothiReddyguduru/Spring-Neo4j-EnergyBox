package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.domain.Gateway;
import org.springframework.stereotype.Service;

import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.service.GatewayService;
import com.energybox.backendcodingchallenge.custom.exception.EntityNotFoundException;
import io.swagger.annotations.ApiOperation;
import com.energybox.backendcodingchallenge.custom.exception.DuplicateEntityFoundException;

import java.util.List;
import java.util.Set;
import java.util.Optional;

@Service
@ApiOperation(value = "Service entry point for Gateway")
public class GatewayService {

    private final GatewayRepository gatewayRepository;

    private final SensorService sensorService;

    public GatewayService(GatewayRepository gatewayRepository, SensorService sensorService) {
        this.gatewayRepository = gatewayRepository;
        this.sensorService = sensorService;
    }

    /**
     * Create or update a gateway if it
     * 
     * @param gateway
     * @return
     *         TO-Do improve this logic
     */
    public Gateway createOrPutGateway(Gateway gateway, boolean isUpdate) {
        // check if gateway is already present for creation;
        Optional<Gateway> gatewayOptional = gatewayRepository.findOneByGatewayId(gateway.getGatewayId());
        // new gateway creation and if it is not a duplicate
        if (gatewayOptional.isPresent() && !isUpdate) {
            throw new DuplicateEntityFoundException("Duplicate Gateway object");
        }
        return gatewayRepository.save(gateway);
    }

    /**
     * Return all gateways if no sensor type is given
     * 
     * @param type
     * @return List<Gateway>
     */
    public List<Gateway> getAllGateways(SensorType type) {
        if (type != null) {
            return getAllGatewaysByType(type);
        }
        return gatewayRepository.findAll();
    }

    private List<Gateway> getAllGatewaysByType(SensorType type) {
        return gatewayRepository.findAllBySensorType(type);
    }

    /**
     * Get a gateway by id
     * 
     * @param id
     * @return optional gateway by id
     */
    public Optional<Gateway> getGatewayById(Long id) {
        return gatewayRepository.findById(id);
    }

    /***
     * Add a given sensor to given gateway if both objects are available
     * else throw exceptions
     * 
     * @param sensorId
     * @param gatewayId
     * @return Gateway
     * 
     */
    public Gateway mapSensorToGatewayById(Long sensorId, Long gatewayId) {
        Optional<Sensor> sensor = sensorService.getSensorById(sensorId);
        if (!sensor.isPresent()) {
            throw new EntityNotFoundException(Sensor.class.getName(), sensorId);
        }
        Optional<Gateway> gateway = getGatewayById(gatewayId);
        if (!gateway.isPresent()) {
            throw new EntityNotFoundException(Gateway.class.getName(), gatewayId);
        }
        // if both are present, add sensor to the list of sensors in gateway object
        sensor.ifPresent(s -> {
            gateway.ifPresent(g -> {
                Set<Sensor> sensors = g.getSensors();
                sensors.add(s);
                g.setSensors(sensors);
                createOrPutGateway(g, true);
            });
        });
        return gateway.get();
    }

}
