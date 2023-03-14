package com.energybox.backendcodingchallenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.HashSet;

/**
 * Tests for gateway controller mappings
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GatewayControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenGetAll_thenReturnAllGateways() throws Exception {
        ResultActions response = mockMvc.perform(get("/gateways"));
        response.andExpect(status().isOk());
    }

    @Test
    public void givenGatewayObject_whenCreateGateway_thenReturnSavedGateway() throws Exception {

        String generatedString = Util.randomString();
        Gateway gateway = new Gateway();
        gateway.setAddress(generatedString);
        gateway.setComments(generatedString);
        gateway.setGatewayId(generatedString);

        ResultActions response = mockMvc.perform(post("/gateways/add", gateway)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway)));

        response.andExpect(status().isCreated());

    }

    @Test
    public void givenDuplicateGatewayObject_whenCreateGateway_thenReturnException() throws Exception {

        String generatedString = Util.randomString();
        Set<Sensor> sensors = new HashSet<Sensor>();
        Gateway gateway = new Gateway();
        gateway.setAddress(generatedString);
        gateway.setComments(generatedString);
        gateway.setGatewayId(generatedString);
        gateway.setSensors(sensors);
        ResultActions response = mockMvc.perform(post("/gateways/add", gateway)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway)));
        
        response.andExpect(status().isCreated());

        String contentAsString = response.andReturn().getResponse().getContentAsString();

        Gateway gatewayResponse = objectMapper.readValue(contentAsString, Gateway.class);

        ResultActions duplicateResponse = mockMvc.perform(post("/gateways/add", gatewayResponse)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway)));

        duplicateResponse.andExpect(status().isNotAcceptable());
                        
    }

    @Test
    public void whenJacksonOnClasspath_thenDefaultContentTypeIsJSON() 
    throws Exception {
        
        // Given
        String expectedMimeType = "application/json";
        
        // Then
        String actualMimeType = mockMvc.perform(get("/gateways"))
        .andReturn().getResponse().getContentType();

        assertThat(expectedMimeType).isEqualTo(actualMimeType);
    }


}
