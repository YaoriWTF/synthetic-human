package com.weyland.bishop;

import com.weyland.synthetic.command.CommandMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bishop")
public class BishopController {

    private final BishopService bishopService;

    public BishopController(BishopService bishopService) {
        this.bishopService = bishopService;
    }

    @PostMapping("/commands")
    public ResponseEntity<String> executeCommand(@RequestBody CommandRequest request) {
        return bishopService.processCommand(request);
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return bishopService.getStatus();
    }

    // Используем record для CommandRequest (Java 16+)
    public record CommandRequest(
            String description,
            CommandMetadata.Priority priority,
            String author
    ) {}
}