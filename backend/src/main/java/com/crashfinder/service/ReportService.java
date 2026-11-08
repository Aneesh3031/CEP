package com.crashfinder.service;

import com.crashfinder.dto.ReportDto;
import com.crashfinder.model.CrashReport;
import com.crashfinder.repository.CrashReportRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportService {

    private final CrashReportRepository crashReportRepository;
    private final ObjectMapper objectMapper;

    public ReportService(CrashReportRepository crashReportRepository, ObjectMapper objectMapper) {
        this.crashReportRepository = crashReportRepository;
        this.objectMapper = objectMapper;
    }

    public List<Map<String, Object>> getReportsForDevice(String deviceUid) {
        List<CrashReport> reports = crashReportRepository.findByDeviceUidOrderByCreatedAtDesc(deviceUid);
        List<Map<String, Object>> list = new ArrayList<>();
        for (CrashReport r : reports) {
            list.add(toFrontendMap(r));
        }
        return list;
    }

    public List<Map<String, Object>> getReportsForUser(String email) {
        List<CrashReport> reports = crashReportRepository.findByUserEmailIgnoreCaseOrderByCreatedAtDesc(email);
        List<Map<String, Object>> list = new ArrayList<>();
        for (CrashReport r : reports) {
            list.add(toFrontendMap(r));
        }
        return list;
    }

    public Optional<Map<String, Object>> getReportById(String reportId) {
        return crashReportRepository.findByReportId(reportId).map(this::toFrontendMap);
    }

    @Transactional
    public Map<String, Object> saveReport(String email, Map<String, Object> data) {
        String reportId = (String) data.getOrDefault("id", "CF-" + (100000 + new Random().nextInt(900000)));
        String deviceUid = (String) data.getOrDefault("deviceId", "dev_default");
        String deviceName = (String) data.getOrDefault("deviceName", "This PC");

        Optional<CrashReport> existingOpt = crashReportRepository.findByReportId(reportId);
        CrashReport report = existingOpt.orElseGet(CrashReport::new);

        report.setReportId(reportId);
        report.setUserEmail(email != null ? email.toLowerCase() : null);
        report.setDeviceUid(deviceUid);
        report.setDeviceName(deviceName);
        report.setFormattedTime(data.get("formattedTime") != null ? data.get("formattedTime").toString() : LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/M/yyyy, h:mm:ss a")));
        report.setResult((String) data.getOrDefault("result", "Healthy"));
        report.setSeverity((String) data.getOrDefault("severity", "PASS"));
        report.setTitle((String) data.getOrDefault("title", "Hardware & Kernel Diagnostic"));
        report.setMsg((String) data.get("msg"));
        report.setFix((String) data.get("fix"));
        report.setComponent((String) data.get("comp"));

        if (data.get("score") instanceof Number) {
            report.setScore(((Number) data.get("score")).intValue());
        }
        if (data.get("deep") instanceof Boolean) {
            report.setDeep((Boolean) data.get("deep"));
        }
        if (data.get("stage1Pass") instanceof Boolean) {
            report.setStage1Pass((Boolean) data.get("stage1Pass"));
        }
        if (data.get("stage2Pass") instanceof Boolean) {
            report.setStage2Pass((Boolean) data.get("stage2Pass"));
        }
        if (data.get("scenario") != null) {
            report.setScenario(data.get("scenario").toString());
        }

        try {
            if (data.containsKey("st")) {
                report.setComponentStatusJson(objectMapper.writeValueAsString(data.get("st")));
            }
            if (data.containsKey("machine")) {
                report.setMachineJson(objectMapper.writeValueAsString(data.get("machine")));
            }
            if (data.containsKey("rootCause")) {
                report.setRootCauseJson(objectMapper.writeValueAsString(data.get("rootCause")));
            }
            if (data.containsKey("logs")) {
                report.setLogsJson(objectMapper.writeValueAsString(data.get("logs")));
            }
            if (data.containsKey("registersAudited")) {
                report.setRegistersAuditedJson(objectMapper.writeValueAsString(data.get("registersAudited")));
            }
        } catch (Exception ignored) {}

        CrashReport saved = crashReportRepository.save(report);
        return toFrontendMap(saved);
    }

    @Transactional
    public void deleteReport(String reportId) {
        crashReportRepository.deleteByReportId(reportId);
    }

    @Transactional
    public void clearDeviceHistory(String deviceUid) {
        crashReportRepository.deleteByDeviceUid(deviceUid);
    }

    public Map<String, Object> toFrontendMap(CrashReport r) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", r.getReportId());
        map.put("reportId", r.getReportId());
        map.put("date", r.getCreatedAt().toString());
        map.put("timestamp", r.getCreatedAt().toString());
        map.put("formattedTime", r.getFormattedTime());
        map.put("result", r.getResult());
        map.put("severity", r.getSeverity());
        map.put("title", r.getTitle());
        map.put("msg", r.getMsg());
        map.put("fix", r.getFix());
        map.put("comp", r.getComponent());
        map.put("score", r.getScore());
        map.put("deep", r.isDeep());
        map.put("stage1Pass", r.getStage1Pass());
        map.put("stage2Pass", r.getStage2Pass());
        map.put("scenario", r.getScenario());
        map.put("deviceId", r.getDeviceUid());
        map.put("deviceName", r.getDeviceName());

        try {
            if (r.getComponentStatusJson() != null) {
                map.put("st", objectMapper.readValue(r.getComponentStatusJson(), new TypeReference<Map<String, Object>>() {}));
            }
            if (r.getMachineJson() != null) {
                map.put("machine", objectMapper.readValue(r.getMachineJson(), new TypeReference<Map<String, Object>>() {}));
            }
            if (r.getRootCauseJson() != null) {
                map.put("rootCause", objectMapper.readValue(r.getRootCauseJson(), new TypeReference<Map<String, Object>>() {}));
            }
            if (r.getLogsJson() != null) {
                map.put("logs", objectMapper.readValue(r.getLogsJson(), new TypeReference<List<Map<String, Object>>>() {}));
            }
            if (r.getRegistersAuditedJson() != null) {
                map.put("registersAudited", objectMapper.readValue(r.getRegistersAuditedJson(), new TypeReference<List<String>>() {}));
            }
        } catch (Exception ignored) {}

        return map;
    }
}

