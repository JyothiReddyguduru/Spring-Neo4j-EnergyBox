package com.energybox.backendcodingchallenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;

// The @Data is a Lombok annotation that generates our getters, setters, 
// equals, hashCode, and toString methods for the domain class.
@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Gateway {

    @Id @GeneratedValue
    private Long id;

    @NonNull
    private String gatewayId;

    @Property("IP address")
    private String address;

    @Property
    private String comments;

    @Relationship(type = "CONNECTED_TO", direction = INCOMING)
    private Set<Sensor> sensors;

}
