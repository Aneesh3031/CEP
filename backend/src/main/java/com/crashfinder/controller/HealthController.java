package com.crashfinder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", "UP");
        map.put("service", "CrashFinder Dynamic Backend");
        map.put("database", "H2 (File Persistent Engine)");
        map.put("storagePath", "./data/crashfinder.mv.db");
        map.put("h2ConsoleUrl", "/h2-console");
        map.put("serverTime", LocalDateTime.now().toString());
        return ResponseEntity.ok(map);
    }
}

