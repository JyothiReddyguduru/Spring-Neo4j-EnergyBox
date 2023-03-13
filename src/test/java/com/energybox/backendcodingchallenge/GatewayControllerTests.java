package com.energybox.backendcodingchallenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;

import com.energybox.backendcodingchallenge.domain.Gateway;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

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

        Gateway gateway = new Gateway();
        gateway.setAddress(generatedString);
        gateway.setComments(generatedString);
        gateway.setGatewayId(generatedString);

        ResultActions response = mockMvc.perform(post("/gateways/add", generatedString)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gateway)));

        response.andExpect(status().isCreated());

        mockMvc.perform(get("/gateways/add", generatedString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}
