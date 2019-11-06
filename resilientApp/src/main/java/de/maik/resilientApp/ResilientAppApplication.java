package de.maik.resilientApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class ResilientAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResilientAppApplication.class, args);
    }

}
