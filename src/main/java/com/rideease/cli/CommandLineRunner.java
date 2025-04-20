package com.rideease.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Spring CommandLineRunner to start the CLI application after Spring Boot has initialized
 */
@Component
public class CommandLineRunner {

    private final RideEaseCLI cli;

    @Autowired
    public CommandLineRunner(RideEaseCLI cli) {
        this.cli = cli;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        System.out.println("RideEase CLI - A Ride Booking System");
        System.out.println("====================================");
        cli.start();
    }
}
