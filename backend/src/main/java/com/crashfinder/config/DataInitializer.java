package com.crashfinder.config;

import com.crashfinder.model.CrashReport;
import com.crashfinder.model.Device;
import com.crashfinder.model.LoginRecord;
import com.crashfinder.model.User;
import com.crashfinder.repository.CrashReportRepository;
import com.crashfinder.repository.DeviceRepository;
import com.crashfinder.repository.LoginRecordRepository;
import com.crashfinder.repository.UserRepository;
import com.crashfinder.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final LoginRecordRepository loginRecordRepository;
    private final DeviceRepository deviceRepository;
    private final CrashReportRepository crashReportRepository;
    private final ObjectMapper objectMapper;

    public DataInitializer(UserRepository userRepository,
                           LoginRecordRepository loginRecordRepository,
                           DeviceRepository deviceRepository,
                           CrashReportRepository crashReportRepository,
                           ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.loginRecordRepository = loginRecordRepository;
        this.deviceRepository = deviceRepository;
        this.crashReportRepository = crashReportRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        try {
            initUsersAndLogins();
            initSampleReport();
            log.info("CrashFinder database initialized successfully with H2 persistent storage.");
        } catch (Exception e) {
            log.warn("Database initialization notice: {}", e.getMessage());
        }
    }

    private void initUsersAndLogins() {
        String demoEmail = "demo@crashfinder.local";
        if (!userRepository.existsByEmailIgnoreCase(demoEmail)) {
            User demo = new User(demoEmail, "Demo User", AuthService.hashPassword("password123"));
            demo.setRole("ADMIN");
            userRepository.save(demo);

            loginRecordRepository.save(new LoginRecord(
                    demoEmail,
                    true,
                    "127.0.0.1",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) CrashFinder Probe/1.0",
                    null
            ));
        }

        String userEmail = "aneesh@crashfinder.local";
        if (!userRepository.existsByEmailIgnoreCase(userEmail)) {
            User user = new User(userEmail, "Aneesh", AuthService.hashPassword("password123"));
            userRepository.save(user);

            loginRecordRepository.save(new LoginRecord(
                    userEmail,
                    true,
                    "192.168.1.105",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/130.0",
                    null
            ));
        }

        // Seed default device for aneesh if none exists
        if (deviceRepository.findByUserEmailIgnoreCaseOrderByCreatedAtAsc(userEmail).isEmpty()) {
            Device dev = new Device();
            dev.setDeviceUid("dev_aneesh_pc");
            dev.setName("Aneesh Workstation");
            dev.setUserEmail(userEmail);
            dev.setHere(true);
            dev.setOs("Microsoft Windows 11 Pro 64-bit");
            dev.setBrowser("Chrome 130");
            dev.setCores("16");
            dev.setRam("31 GB (2x modules)");
            dev.setScreen("2560×1440");
            dev.setLang("en-US");
            dev.setSettingsJson("{\"ram\":true,\"pcie\":true,\"storage\":true,\"software\":true,\"deep\":true,\"plain\":true,\"remind\":true}");
            deviceRepository.save(dev);
        }
    }

    private void initSampleReport() {
        if (crashReportRepository.count() > 0) {
            return;
        }

        Map<String, Object> sampleData = null;

        // Try reading reports/CF-735237.json
        File[] candidateFiles = new File[] {
                new File("reports/CF-735237.json"),
                new File("../reports/CF-735237.json"),
                new File("src/main/resources/static/reports/CF-735237.json")
        };

        for (File f : candidateFiles) {
            if (f.exists()) {
                try {
                    sampleData = objectMapper.readValue(f, new TypeReference<Map<String, Object>>() {});
                    break;
                } catch (Exception ignored) {}
            }
        }

        if (sampleData == null) {
            try (InputStream is = getClass().getResourceAsStream("/static/reports/CF-735237.json")) {
                if (is != null) {
                    sampleData = objectMapper.readValue(is, new TypeReference<Map<String, Object>>() {});
                }
            } catch (Exception ignored) {}
        }

        if (sampleData != null) {
            CrashReport cr = new CrashReport();
            cr.setReportId(sampleData.getOrDefault("reportId", "CF-735237").toString());
            cr.setUserEmail("aneesh@crashfinder.local");
            cr.setDeviceUid("dev_aneesh_pc");
            cr.setDeviceName("Aneesh Workstation");
            cr.setFormattedTime(sampleData.getOrDefault("formattedTime", "6/10/2026, 8:22:53 pm").toString());
            cr.setResult("Healthy");
            cr.setSeverity("PASS");
            cr.setScore(98);
            cr.setDeep(true);
            cr.setStage1Pass(true);
            cr.setStage2Pass(true);
            cr.setScenario("none");

            if (sampleData.containsKey("rootCause") && sampleData.get("rootCause") instanceof Map) {
                Map<?, ?> rc = (Map<?, ?>) sampleData.get("rootCause");
                cr.setTitle(rc.get("title") != null ? rc.get("title").toString() : "System Nominal & Fully Stable");
                cr.setMsg(rc.get("explanation") != null ? rc.get("explanation").toString() : "");
                cr.setFix(rc.get("recommendation") != null ? rc.get("recommendation").toString() : "");
            }

            try {
                cr.setMachineJson(objectMapper.writeValueAsString(sampleData.get("machine")));
                cr.setRootCauseJson(objectMapper.writeValueAsString(sampleData.get("rootCause")));
                cr.setLogsJson(objectMapper.writeValueAsString(sampleData.get("logs")));
                cr.setRegistersAuditedJson(objectMapper.writeValueAsString(sampleData.get("registersAudited")));
                cr.setComponentStatusJson("{\"ram\":\"ok\",\"pcie\":\"ok\",\"storage\":\"ok\",\"driver\":\"ok\",\"os\":\"ok\",\"malware\":\"ok\"}");
            } catch (Exception ignored) {}

            crashReportRepository.save(cr);
            log.info("Imported seed diagnostic report {} into database", cr.getReportId());
        }
    }
}

