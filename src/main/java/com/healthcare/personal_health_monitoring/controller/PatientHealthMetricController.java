package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.entity.HealthMetricType;
import com.healthcare.personal_health_monitoring.entity.PatientHealthMetric;
import com.healthcare.personal_health_monitoring.service.PatientHealthMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class PatientHealthMetricController {

    private final PatientHealthMetricService metricService;

    @PostMapping("/{patientId}")
    public PatientHealthMetric addMetric(
            @PathVariable Long patientId,
            @RequestParam HealthMetricType metricType,
            @RequestParam Double value) {

        return metricService.addMetric(patientId, metricType, value);
    }

    @GetMapping("/{patientId}/graph")
    public List<PatientHealthMetric> getGraphData(
            @PathVariable Long patientId,
            @RequestParam HealthMetricType metricType) {

        return metricService.getMetricsForGraph(patientId, metricType);
    }
}

