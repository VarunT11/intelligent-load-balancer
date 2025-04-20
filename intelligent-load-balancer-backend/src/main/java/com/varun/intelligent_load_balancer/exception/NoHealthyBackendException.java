package com.varun.intelligent_load_balancer.exception;

public class NoHealthyBackendException extends IllegalArgumentException {
    public NoHealthyBackendException(String routePath){
        super("No Healthy Backends found for Route: " + routePath);
    }
}
