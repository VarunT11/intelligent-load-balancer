package com.varun.intelligent_load_balancer.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRouteDefinitionException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRoute(InvalidRouteDefinitionException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidPropertyException.class)
    public ResponseEntity<Map<String, String>> handleInvalidProperty(InvalidPropertyException ex){
        Map<String,String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidSessionNameException.class)
    public ResponseEntity<Map<String,String>> handleInvalidSessionName(InvalidSessionNameException ex){
        Map<String,String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

}
