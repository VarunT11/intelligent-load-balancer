package com.varun.intelligent_load_balancer.dto.config;

public class SystemPropertyRequestDTO {

    private String key;
    private String value;

    public SystemPropertyRequestDTO(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
