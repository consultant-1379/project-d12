package com.ericsson.graduates.microservicemanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@SpringBootApplication
public class MicroserviceManagerApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(MicroserviceManagerApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(MicroserviceManagerApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(MicroserviceRepository repository) {
		return (args) -> {
//			Microservice msA = new Microservice("serv1", null, "some service", new Date(), Arrays.asList("0.0.1", "0.0.2", "0.0.3"), "cat");
//			Microservice msB = new Microservice("serv2", null, "a service", new Date(), new ArrayList<>(Collections.singletonList("0.0.2")), "cat");
//			Microservice msC = new Microservice("serv3", null, "the service", new Date(), new ArrayList<>(Collections.singletonList("0.0.3")), "cat");
//			Microservice msD = new Microservice("serv4", null, "the service", new Date(), new ArrayList<>(Collections.singletonList("0.0.4")), "cat");
//			Microservice msE = new Microservice("serv5", null, "the service", new Date(), new ArrayList<>(Collections.singletonList("0.0.5")), "cat");
//
//			msB.addDependency(msA);
//
//			repository.save(msA);
//			repository.save(msB);
//			repository.save(msC);
//			repository.save(msD);
//			repository.save(msE);

//			LOGGER.info("Searching for all microservices...");
//			LOGGER.info("----------------------------------");
//			for (Microservice microservice : repository.findAll()) {
//				LOGGER.info(microservice.toString());
//			}
		};
	}
}
