package com.crashfinder.controller;

import com.crashfinder.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getReports(
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String email) {
        if (deviceId != null && !deviceId.trim().isEmpty()) {
            return ResponseEntity.ok(reportService.getReportsForDevice(deviceId));
        } else if (email != null && !email.trim().isEmpty()) {
            return ResponseEntity.ok(reportService.getReportsForUser(email));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReportById(@PathVariable String reportId) {
        return reportService.getReportById(reportId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> saveReport(
            @RequestParam(required = false) String email,
            @RequestBody Map<String, Object> reportData) {
        Map<String, Object> saved = reportService.saveReport(email, reportData);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable String reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.ok(Collections.singletonMap("status", "deleted"));
    }

    @DeleteMapping("/device/{deviceUid}")
    public ResponseEntity<?> clearDeviceHistory(@PathVariable String deviceUid) {
        reportService.clearDeviceHistory(deviceUid);
        return ResponseEntity.ok(Collections.singletonMap("status", "cleared"));
    }
}

