package com.varun.intelligent_load_balancer.controller.admin;

import com.varun.intelligent_load_balancer.dto.metrics.MetricsSessionResponseDTO;
import com.varun.intelligent_load_balancer.dto.metrics.MetricsSnapshotDTO;
import com.varun.intelligent_load_balancer.mapper.MetricsMapper;
import com.varun.intelligent_load_balancer.model.metrics.Metrics;
import com.varun.intelligent_load_balancer.model.metrics.MetricsSession;
import com.varun.intelligent_load_balancer.service.metrics.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/metrics")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService){
        this.metricsService = metricsService;
    }

    @GetMapping("")
    public ResponseEntity<MetricsSnapshotDTO> getLiveMetrics(){
        Metrics metrics = metricsService.getLiveSnapshot();
        return ResponseEntity.ok(MetricsMapper.mapToMetricsSnapshotDTO(metrics, metricsService.getLiveMetricsStartTime()));
    }

    @PostMapping("/sessions")
    public ResponseEntity<MetricsSessionResponseDTO> saveSnapshot(@RequestParam String sessionName) {
        MetricsSession session = metricsService.saveSnapshot(sessionName);
        return ResponseEntity.ok(MetricsMapper.mapToMetricsSessionResponseDTO(session));
    }

    @PostMapping("/reset")
    public ResponseEntity<MetricsSnapshotDTO> resetLiveMetrics(){
        metricsService.resetLiveMetrics();
        Metrics metrics = metricsService.getLiveSnapshot();
        return ResponseEntity.ok(MetricsMapper.mapToMetricsSnapshotDTO(metrics, metricsService.getLiveMetricsStartTime()));
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<MetricsSessionResponseDTO>> getAllMetrics(){
        List<MetricsSessionResponseDTO> metricsSessions =
                metricsService
                        .getAllSnapshots()
                        .stream()
                        .map(MetricsMapper::mapToMetricsSessionResponseDTO)
                        .toList();
        return ResponseEntity.ok(metricsSessions);
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<MetricsSessionResponseDTO> getMetricSessionById(@PathVariable Long id) {
        Optional<MetricsSession> metricsSession = metricsService.getSnapshotById(id);
        return metricsSession.map(session -> ResponseEntity.ok(MetricsMapper.mapToMetricsSessionResponseDTO(session))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/sessions/{id}")
    public void deleteSession(@PathVariable Long id){
        metricsService.deleteSnapshotById(id);
    }

}
