package com.energybox.backendcodingchallenge;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;

import com.energybox.backendcodingchallenge.controller.GatewayController;

@SpringBootTest
@ComponentScan("com.energybox.backendcodingchallenge")
class BackendCodingChallengeApplicationTests {

    @Autowired
	private GatewayController controller;

	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

}
