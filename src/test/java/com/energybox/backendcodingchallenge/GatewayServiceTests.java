package com.energybox.backendcodingchallenge;

import org.junit.jupiter.api.Test;

import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.service.GatewayService;
import com.energybox.backendcodingchallenge.service.SensorService;
import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

/**
 * Tests for gateway service
 */
@SpringBootTest
public class GatewayServiceTests {

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private SensorService sensorService;

    @Test
    void when_getAllGateways_thenReturnAllGateways() {

        // set up
        String generatedString = "serviceGateway*";
        Gateway gateway = new Gateway();
        gateway.setAddress(generatedString);
        gateway.setComments(generatedString);
        gateway.setGatewayId(generatedString);
        // perform
        gatewayService.createOrPutGateway(gateway, false);
        List<Gateway> gateways = gatewayService.getAllGateways(null);

        // assert
        Assert.isTrue(gateways.stream().anyMatch(t -> t.getGatewayId()
                .equals(generatedString)), "Gateway list is incorrect");
    }

    @Test
    void when_getAllGatewayBySensorType_thenReturnAllGatewaysByType() {

        String genString = Util.randomString();
        Sensor sensor = new Sensor();
        sensor.setComments(genString);
        sensor.setSensorId(genString);
        Set<SensorType> types = new HashSet<SensorType>();
        types.add(SensorType.HUMIDITY);
        sensor.setSensorTypes(types);
        sensor = sensorService.createOrPutSensor(sensor, false);

        String generatedString = Util.randomString();
        Gateway gateway = new Gateway();
        gateway.setAddress(generatedString);
        gateway.setComments(generatedString);
        gateway.setGatewayId(generatedString);
        gateway = gatewayService.createOrPutGateway(gateway, false);

        Set<Sensor> sensors = new HashSet<>();
        sensors.add(sensor);
        gateway.setSensors(sensors);
        gateway = gatewayService.createOrPutGateway(gateway, true);

        // perform
        List<Gateway> gateways = gatewayService.getAllGateways(SensorType.HUMIDITY);
        // test
        Assert.isTrue(!gateways.isEmpty(), "Gateways cannot be empty");
        Optional<Gateway> gatewayEntry = gateways.stream().filter(t -> t.getGatewayId().equals(generatedString))
                .findAny();

        Assert.isTrue(gatewayEntry.isPresent(), "Gateway cannot be empty");

    }

}
