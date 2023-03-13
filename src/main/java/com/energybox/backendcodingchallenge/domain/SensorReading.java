package com.energybox.backendcodingchallenge.domain;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
@Node
@RequiredArgsConstructor
public class SensorReading {

    @Id @GeneratedValue
    private Long id;
    
    @Property
    @NonNull
    private Date lastReadDate;

    @Property
    @NonNull
    private String value;
}
