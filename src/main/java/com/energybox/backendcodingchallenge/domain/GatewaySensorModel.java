package com.energybox.backendcodingchallenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GatewaySensorModel {
    
    Long gatewayId;
    Long sensorId;
}
