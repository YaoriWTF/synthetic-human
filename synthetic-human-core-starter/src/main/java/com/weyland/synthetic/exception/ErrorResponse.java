package com.weyland.synthetic.exception;

import java.time.Instant;

public record ErrorResponse(String message, String code, Instant timestamp) {
    public ErrorResponse(String message, String code) {
        this(message, code, Instant.now());
    }
}