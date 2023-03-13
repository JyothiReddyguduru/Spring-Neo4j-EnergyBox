package com.energybox.backendcodingchallenge.custom.exception.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket swaggerApi() {
       return new Docket(DocumentationType.SWAGGER_2).select()
          .apis(RequestHandlerSelectors.basePackage("com.energybox.backendcodingchallenge")).build();
    }
}