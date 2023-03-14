package com.energybox.backendcodingchallenge.service;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.Optional;

import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.service.GatewayService;
import com.energybox.backendcodingchallenge.custom.exception.EntityNotFoundException;
import com.energybox.backendcodingchallenge.custom.exception.DuplicateEntityFoundException;
import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.domain.Gateway;

@Service
public class GatewayService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GatewayService.class);

    private final GatewayRepository gatewayRepository;

    private final SensorService sensorService;

    public GatewayService(GatewayRepository gatewayRepository, SensorService sensorService) {
        this.gatewayRepository = gatewayRepository;
        this.sensorService = sensorService;
    }

    /**
     * Create or update a gateway if it
     * @param gateway
     * @return
     */
    public Gateway createOrPutGateway(Gateway gateway, boolean isUpdate) {
        // check if gateway is already present for creation;
        Optional<Gateway> gatewayOptional = gatewayRepository.findOneByGatewayId(gateway.getGatewayId());
        // new gateway creation and if it is not a duplicate
        if (gatewayOptional.isPresent() && !isUpdate) {
            LOGGER.error("Duplicate Gateway object");
            throw new DuplicateEntityFoundException("Duplicate Gateway object for gatewayId " + gateway.getGatewayId());
        }
        LOGGER.info(isUpdate ? "Gateway object updated": "Gateway object created");
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

    /**
     * Return all gateways that have a sensor of given sensor type
     * @param SensorType
     * @return all gateway by sensor type
     */
    private List<Gateway> getAllGatewaysByType(SensorType type) {
        return gatewayRepository.findAllBySensorType(type);
    }

    /**
     * Get a gateway by id
     * @param id
     * @return optional gateway by id
     */
    public Optional<Gateway> getGatewayById(Long id) {
        return gatewayRepository.findById(id);
    }

    /***
     * Add a given sensor to given gateway if both objects are available
     * else throw exceptions
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
        // check if sensor is connected to any gateway
        // TO-DO check if Sensor already mapped to Gateway
        // if both are present, add sensor to the list of sensors in gateway object
        sensor.ifPresent(s -> {
            gateway.ifPresent(g -> {
                Set<Sensor> sensors = g.getSensors();
                boolean found = sensors.stream().anyMatch(t -> t.getSensorId().equals(s.getSensorId()));
                if(found) {
                    throw new RuntimeException("Sensor already mapped to Gateway");
                }
                sensors.add(s);
                g.setSensors(sensors);
                createOrPutGateway(g, true);
                LOGGER.info("Sensor " + sensor.get().getSensorId() + " is mapped to Gateway"
                        + gateway.get().getGatewayId());
            });
        });
        return getGatewayById(gatewayId).get();
    }

}
