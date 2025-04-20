package com.varun.intelligent_load_balancer.exception;

public class RouteNotFoundException extends IllegalArgumentException{
    public RouteNotFoundException(String routePath){
        super("No routes found the following path: " + routePath);
    }
}
