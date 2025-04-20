package com.rideease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RideEaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideEaseApplication.class, args);
        // The CLI will be started by com.rideease.cli.CommandLineRunner component
    }
}
