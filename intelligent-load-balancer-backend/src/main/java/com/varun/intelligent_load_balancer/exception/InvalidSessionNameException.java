package com.varun.intelligent_load_balancer.exception;

public class InvalidSessionNameException extends RuntimeException {
    public InvalidSessionNameException(String message) {
        super(message);
    }
}
