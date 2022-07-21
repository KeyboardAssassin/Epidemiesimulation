package com.codewithdani;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class CoronaSimulationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoronaSimulationApplication.class, args);
	}

}
