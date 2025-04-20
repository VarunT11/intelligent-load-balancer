package com.varun.intelligent_load_balancer.exception;

public class InvalidRouteDefinitionException extends RuntimeException {
    public InvalidRouteDefinitionException(String message){
        super(message);
    }
}
