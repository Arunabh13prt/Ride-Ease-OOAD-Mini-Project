package com.rideease.patterns.singleton;

/**
 * Singleton Pattern Implementation
 * Ensures only one instance of database connection exists throughout the application
 * Team Member: Member 1
 */
public class DatabaseConnector {
    
    private static DatabaseConnector instance;
    private final String connectionString;
    private final String username;
    private final String password;
    private boolean connected;
    
    // Private constructor to prevent instantiation from outside
    private DatabaseConnector() {
        this.connectionString = "jdbc:mysql://localhost:3306/rideease";
        this.username = "root";
        this.password = "aryan";
        this.connected = false;
    }
    
    // Method to get the singleton instance
    public static synchronized DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }
    
    // Methods to use the database connection
    public void connect() {
        if (!connected) {
            System.out.println("Connecting to database: " + connectionString);
            // In a real application, this would use JDBC to connect
            connected = true;
            System.out.println("Connected successfully");
        } else {
            System.out.println("Already connected to database");
        }
    }
    
    public void disconnect() {
        if (connected) {
            System.out.println("Disconnecting from database");
            // In a real application, this would close the JDBC connection
            connected = false;
            System.out.println("Disconnected successfully");
        } else {
            System.out.println("Not connected to database");
        }
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public String getConnectionInfo() {
        return "Connection to " + connectionString + " as " + username;
    }
}
