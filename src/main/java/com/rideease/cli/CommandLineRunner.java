package com.rideease.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Spring CommandLineRunner to start the CLI application after Spring Boot has initialized
 * Can be disabled by setting app.cli.enabled=false in application.properties
 */
@Component
public class CommandLineRunner {

    private final RideEaseCLI cli;
    
    @Value("${app.cli.enabled:false}")
    private boolean cliEnabled;

    @Autowired
    public CommandLineRunner(RideEaseCLI cli) {
        this.cli = cli;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        if (cliEnabled) {
            System.out.println("RideEase CLI - A Ride Booking System");
            System.out.println("====================================");
            cli.start();
        } else {
            System.out.println("RideEase Web Interface is running");
            System.out.println("Access the web interface at http://localhost:8080");
        }
    }
}
