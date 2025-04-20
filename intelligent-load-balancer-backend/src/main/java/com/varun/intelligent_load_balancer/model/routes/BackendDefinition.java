package com.varun.intelligent_load_balancer.model.routes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;

import java.util.Objects;

@Embeddable
public class BackendDefinition {

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String url;

    public BackendDefinition(){

    }

    public BackendDefinition(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackendDefinition that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }
}
