package com.weyland.synthetic.exception;

public class SyntheticHumanException extends RuntimeException {
    public SyntheticHumanException(String message) {
        super(message);
    }

    public SyntheticHumanException(String message, Throwable cause) {
        super(message, cause);
    }
}