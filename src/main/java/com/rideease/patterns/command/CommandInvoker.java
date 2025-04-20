package com.rideease.patterns.command;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Command Pattern: Invoker
 * Invokes commands and maintains a history of executed commands
 * Team Member: Member 4
 */
@Component
public class CommandInvoker {
    
    private final List<Command> commandHistory = new ArrayList<>();
    
    public Object executeCommand(Command command) {
        Object result = command.execute();
        commandHistory.add(command);
        return result;
    }
    
    public List<Command> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }
    
    public void clearCommandHistory() {
        commandHistory.clear();
    }
}
