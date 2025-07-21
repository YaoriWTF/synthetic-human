package com.weyland.bishop;

import com.weyland.synthetic.command.Command;
import com.weyland.synthetic.command.CommandExecutor;
import com.weyland.synthetic.command.CommandMetadata;
import com.weyland.synthetic.command.LoggingCommand;
import com.weyland.synthetic.monitoring.ActivityMonitor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class BishopService {
    private final CommandExecutor commandExecutor;
    private final ActivityMonitor activityMonitor;

    public BishopService(CommandExecutor commandExecutor, ActivityMonitor activityMonitor) {
        this.commandExecutor = commandExecutor;
        this.activityMonitor = activityMonitor;
    }

    public ResponseEntity<String> processCommand(BishopController.CommandRequest request) {
        try {
            CommandMetadata metadata = new CommandMetadata(request.description(), // Используем методы record
                    request.priority(), request.author(), Instant.now());

            Command<String> command = new LoggingCommand(metadata);
            commandExecutor.execute(command);

            return ResponseEntity.accepted().body("Command accepted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Bishop status: Active tasks - " + activityMonitor.getActiveTasksCount());
    }
}