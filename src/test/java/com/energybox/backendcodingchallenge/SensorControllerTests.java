package com.energybox.backendcodingchallenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;

import com.energybox.backendcodingchallenge.custom.models.Enums.SensorType;
import com.energybox.backendcodingchallenge.domain.Sensor;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
/**
 * Tests for gateway controller mappings
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SensorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenGetAll_thenReturnAllGateways() throws Exception{
        ResultActions response = mockMvc.perform(get("/sensors"));
            response.andExpect(status().isOk());
    }

   @Test
    public void givenGatewayObject_whenCreateGateway_thenReturnSavedGateway() throws Exception{

        String generatedString = randomString();
        Sensor sensor = new Sensor();
        sensor.setComments(generatedString);
        sensor.setSensorId(generatedString);
        Set<SensorType> types = new HashSet<SensorType>();
        types.add(SensorType.HUMIDITY);
        sensor.setSensorTypes(types);

        ResultActions response = mockMvc.perform(post("/sensors/add", sensor)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sensor)));

            response.andExpect(status().isCreated());

    }

    @Test
    public void givenDuplicateSensorObject_whenCreateSensor_thenReturnException() throws Exception{
        
        String generatedString = randomString();
        Sensor sensor = new Sensor();
        sensor.setComments(generatedString);
        sensor.setSensorId(generatedString);
        Set<SensorType> types = new HashSet<SensorType>();
        types.add(SensorType.HUMIDITY);
        sensor.setSensorTypes(types);

        ResultActions response = mockMvc.perform(post("/sensors/add", sensor)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sensor)));

            response.andExpect(status().isCreated());
        
            response = mockMvc.perform(post("/sensors/add", sensor)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sensor)));

            response.andExpect(status().isNotAcceptable());

    }

    private static final String CHAR_LIST = 
	        "abcdefghijklmnopqrstuvwxyz";
	private static final int RANDOM_STRING_LENGTH = 3;
	public String randomString(){
		StringBuffer randomString = new StringBuffer();
                for(int i=0; i<RANDOM_STRING_LENGTH; i++){
                    int number = getRandomNumber();
                    char ch = CHAR_LIST.charAt(number);
                    randomString.append(ch);
               }
               return randomString.toString();
        }
    
        private int getRandomNumber() {
            int randomInt = 0;
            Random randomGenerator = new Random();
            randomInt = randomGenerator.nextInt(CHAR_LIST.length());
            if (randomInt - 1 == -1) {
               return randomInt;
            } else {
             return randomInt - 1;
           }
         }

    
}
