package com.energybox.backendcodingchallenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.NoArgsConstructor;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;
import java.util.List;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sensor {

    @Id @GeneratedValue
    private Long id;

    @NonNull
    private String sensorId;

    @Property
    private String comments;

    @Property
    private Set<SensorType> sensorTypes;

    @Relationship(type = "HAS")
    private Set<SensorReading> readings;
}
