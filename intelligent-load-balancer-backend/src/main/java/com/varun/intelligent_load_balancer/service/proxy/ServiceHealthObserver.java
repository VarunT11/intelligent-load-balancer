package com.varun.intelligent_load_balancer.service.proxy;

import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;

public interface ServiceHealthObserver {
    void onSuccess(BackendDefinition backendDefinition);
    void onFailure(BackendDefinition backendDefinition);
}
