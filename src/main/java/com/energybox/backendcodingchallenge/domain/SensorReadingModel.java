package com.energybox.backendcodingchallenge.domain;

import lombok.Data;

@Data
public class SensorReadingModel {
    
    private Long sensorId;

    private String value;
}
