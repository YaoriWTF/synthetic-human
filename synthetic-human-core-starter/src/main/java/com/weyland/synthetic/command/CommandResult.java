package com.weyland.synthetic.command;

public record CommandResult<T>(T result, Status status, String message) {
    public enum Status {
        SUCCESS, FAILURE
    }
}