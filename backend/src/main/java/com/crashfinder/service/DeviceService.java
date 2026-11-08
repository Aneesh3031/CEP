package com.crashfinder.service;

import com.crashfinder.dto.DeviceDto;
import com.crashfinder.model.Device;
import com.crashfinder.repository.CrashReportRepository;
import com.crashfinder.repository.DeviceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final CrashReportRepository crashReportRepository;
    private final ObjectMapper objectMapper;

    public DeviceService(DeviceRepository deviceRepository, CrashReportRepository crashReportRepository, ObjectMapper objectMapper) {
        this.deviceRepository = deviceRepository;
        this.crashReportRepository = crashReportRepository;
        this.objectMapper = objectMapper;
    }

    public List<Map<String, Object>> getDevicesForUser(String email) {
        List<Device> devices = deviceRepository.findByUserEmailIgnoreCaseOrderByCreatedAtAsc(email);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Device d : devices) {
            result.add(toMap(d));
        }
        return result;
    }

    @Transactional
    public Map<String, Object> createOrUpdateDevice(String email, Map<String, Object> deviceMap) {
        String uid = (String) deviceMap.get("id");
        if (uid == null || uid.trim().isEmpty()) {
            uid = "dev_" + System.currentTimeMillis();
        }

        String name = (String) deviceMap.getOrDefault("name", "This PC");
        boolean here = Boolean.TRUE.equals(deviceMap.get("here"));

        Optional<Device> existing = deviceRepository.findByDeviceUid(uid);
        Device device = existing.orElseGet(Device::new);

        device.setDeviceUid(uid);
        device.setName(name);
        device.setUserEmail(email.toLowerCase());
        device.setHere(here);
        device.setUpdatedAt(LocalDateTime.now());

        if (deviceMap.containsKey("info") && deviceMap.get("info") instanceof Map) {
            Map<?, ?> info = (Map<?, ?>) deviceMap.get("info");
            device.setOs(info.get("os") != null ? info.get("os").toString() : null);
            device.setBrowser(info.get("browser") != null ? info.get("browser").toString() : null);
            device.setCores(info.get("cores") != null ? info.get("cores").toString() : null);
            device.setRam(info.get("ram") != null ? info.get("ram").toString() : null);
            device.setScreen(info.get("screen") != null ? info.get("screen").toString() : null);
            device.setLang(info.get("lang") != null ? info.get("lang").toString() : null);
        }

        if (deviceMap.containsKey("settings")) {
            try {
                device.setSettingsJson(objectMapper.writeValueAsString(deviceMap.get("settings")));
            } catch (Exception ignored) {}
        }

        Device saved = deviceRepository.save(device);
        return toMap(saved);
    }

    @Transactional
    public void deleteDevice(String uid) {
        deviceRepository.deleteByDeviceUid(uid);
        crashReportRepository.deleteByDeviceUid(uid);
    }

    private Map<String, Object> toMap(Device d) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", d.getDeviceUid());
        map.put("name", d.getName());
        map.put("here", d.isHere());

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("os", d.getOs() != null ? d.getOs() : "Windows");
        info.put("browser", d.getBrowser() != null ? d.getBrowser() : "Browser");
        info.put("cores", d.getCores() != null ? d.getCores() : "n/a");
        info.put("ram", d.getRam() != null ? d.getRam() : "n/a");
        info.put("screen", d.getScreen() != null ? d.getScreen() : "n/a");
        info.put("lang", d.getLang() != null ? d.getLang() : "en-US");
        map.put("info", info);

        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("ram", true);
        settings.put("pcie", true);
        settings.put("storage", true);
        settings.put("software", true);
        settings.put("deep", false);
        settings.put("plain", true);
        settings.put("remind", true);

        if (d.getSettingsJson() != null && !d.getSettingsJson().isEmpty()) {
            try {
                Map<String, Object> parsed = objectMapper.readValue(d.getSettingsJson(), new TypeReference<>() {});
                settings.putAll(parsed);
            } catch (Exception ignored) {}
        }
        map.put("settings", settings);

        return map;
    }
}

