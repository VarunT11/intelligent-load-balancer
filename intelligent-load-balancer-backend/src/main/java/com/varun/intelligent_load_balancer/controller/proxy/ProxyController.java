package com.varun.intelligent_load_balancer.controller.proxy;

import com.varun.intelligent_load_balancer.service.proxy.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProxyController {

    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @RequestMapping("**")
    public ResponseEntity<?> proxyRequest(@RequestHeader HttpHeaders headers, @RequestBody(required = false) String body,
                                          HttpMethod method, @RequestParam(required = false) MultiValueMap<String, String> params,
                                          HttpServletRequest request) {
        String path = request.getRequestURI();
        if(path.startsWith("/admin")){
            return ResponseEntity.notFound().build();
        }

        return proxyService.proxyRequest(headers, body, method, request);
    }

}
