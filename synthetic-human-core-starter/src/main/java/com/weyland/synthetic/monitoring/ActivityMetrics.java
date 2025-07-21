package com.weyland.synthetic.monitoring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/activity")
public class ActivityMetrics {
    private final ActivityMonitor activityMonitor;

    public ActivityMetrics(ActivityMonitor activityMonitor) {
        this.activityMonitor = activityMonitor;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getActivityStatus() {
        return ResponseEntity.ok(Map.of(
                "activeTasks", activityMonitor.getActiveTasksCount(),
                "timestamp", Instant.now()
        ));
    }
}