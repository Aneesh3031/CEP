package com.crashfinder.service;

import com.crashfinder.dto.StatsDto;
import com.crashfinder.model.CrashReport;
import com.crashfinder.model.LoginRecord;
import com.crashfinder.repository.CrashReportRepository;
import com.crashfinder.repository.DeviceRepository;
import com.crashfinder.repository.LoginRecordRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StatsService {

    private final CrashReportRepository crashReportRepository;
    private final LoginRecordRepository loginRecordRepository;
    private final DeviceRepository deviceRepository;

    public StatsService(CrashReportRepository crashReportRepository,
                        LoginRecordRepository loginRecordRepository,
                        DeviceRepository deviceRepository) {
        this.crashReportRepository = crashReportRepository;
        this.loginRecordRepository = loginRecordRepository;
        this.deviceRepository = deviceRepository;
    }

    public StatsDto getStats() {
        StatsDto dto = new StatsDto();

        long totalScans = crashReportRepository.count();
        dto.setTotalScans(totalScans);

        long totalLogins = loginRecordRepository.count();
        dto.setTotalLogins(totalLogins);

        long successLogins = loginRecordRepository.countBySuccess(true);
        dto.setSuccessfulLogins(successLogins);
        dto.setFailedLogins(totalLogins - successLogins);

        dto.setTotalDevices(deviceRepository.count());

        long faults = crashReportRepository.countByResultNot("Healthy");
        dto.setTotalFaults(faults);

        List<CrashReport> reports = crashReportRepository.findAll();
        if (!reports.isEmpty()) {
            double avg = reports.stream()
                    .filter(r -> r.getScore() != null)
                    .mapToInt(CrashReport::getScore)
                    .average()
                    .orElse(100.0);
            dto.setAverageHealthScore(Math.round(avg * 10.0) / 10.0);
        } else {
            dto.setAverageHealthScore(100.0);
        }

        // Recent logins
        List<LoginRecord> recentLogins = loginRecordRepository.findTop50ByOrderByLoginTimeDesc();
        List<Map<String, Object>> loginMaps = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (LoginRecord lr : recentLogins) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", lr.getId());
            m.put("email", lr.getEmail());
            m.put("loginTime", lr.getLoginTime().format(dtf));
            m.put("success", lr.isSuccess());
            m.put("ipAddress", lr.getIpAddress());
            m.put("userAgent", lr.getUserAgent());
            m.put("failureReason", lr.getFailureReason());
            loginMaps.add(m);
        }
        dto.setRecentLogins(loginMaps);

        // Recent reports
        List<CrashReport> recentReports = crashReportRepository.findTop20ByOrderByCreatedAtDesc();
        List<Map<String, Object>> reportMaps = new ArrayList<>();
        for (CrashReport r : recentReports) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", r.getReportId());
            m.put("title", r.getTitle());
            m.put("result", r.getResult());
            m.put("score", r.getScore());
            m.put("date", r.getCreatedAt().format(dtf));
            m.put("device", r.getDeviceName());
            reportMaps.add(m);
        }
        dto.setRecentReports(reportMaps);

        return dto;
    }
}

