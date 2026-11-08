package com.crashfinder.controller;

import com.crashfinder.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getDevices(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(deviceService.getDevicesForUser(email));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> saveDevice(@RequestParam String email,
                                                          @RequestBody Map<String, Object> deviceMap) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(deviceService.createOrUpdateDevice(email, deviceMap));
    }

    @DeleteMapping("/{deviceUid}")
    public ResponseEntity<?> deleteDevice(@PathVariable String deviceUid) {
        deviceService.deleteDevice(deviceUid);
        return ResponseEntity.ok(Collections.singletonMap("status", "deleted"));
    }
}

