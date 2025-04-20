package com.varun.intelligent_load_balancer.service.proxy.canaryRouting;

import com.varun.intelligent_load_balancer.model.routes.BackendDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CanaryRoutingService {

    private final Random random = new Random();

    public boolean isRequestCanaryCandidate(List<BackendDefinition> canary, int canaryTrafficPercent){
        return canary != null && !canary.isEmpty() && canaryTrafficPercent > 0 && random.nextInt(100) <= canaryTrafficPercent;
    }

}
