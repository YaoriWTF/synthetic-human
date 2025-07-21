package com.weyland.synthetic.command;

import java.time.Instant;

public record CommandMetadata(
        String description,
        Priority priority,
        String author,
        Instant time
) {
    public enum Priority {
        COMMON, CRITICAL
    }
}
