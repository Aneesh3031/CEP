package com.crashfinder.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "crash_reports")
public class CrashReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String reportId;

    @Column(length = 180)
    private String userEmail;

    @Column(length = 80)
    private String deviceUid;

    @Column(length = 120)
    private String deviceName;

    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(length = 80)
    private String formattedTime;

    @Column(length = 80)
    private String result; // Healthy, Hardware fault, Software issue, PASS, CRITICAL

    @Column(length = 50)
    private String severity = "PASS";

    @Column(length = 255)
    private String title;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String msg;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String fix;

    @Column(length = 100)
    private String component;

    private Integer score = 100;

    private boolean deep = false;

    private Boolean stage1Pass = true;
    private Boolean stage2Pass = true;

    @Column(length = 80)
    private String scenario = "none";

    @Lob
    @Column(columnDefinition = "CLOB")
    private String componentStatusJson;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String machineJson;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String rootCauseJson;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String logsJson;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String registersAuditedJson;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public CrashReport() {}

    public Long getId() {
        return id;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDeviceUid() {
        return deviceUid;
    }

    public void setDeviceUid(String deviceUid) {
        this.deviceUid = deviceUid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFix() {
        return fix;
    }

    public void setFix(String fix) {
        this.fix = fix;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public boolean isDeep() {
        return deep;
    }

    public void setDeep(boolean deep) {
        this.deep = deep;
    }

    public Boolean getStage1Pass() {
        return stage1Pass;
    }

    public void setStage1Pass(Boolean stage1Pass) {
        this.stage1Pass = stage1Pass;
    }

    public Boolean getStage2Pass() {
        return stage2Pass;
    }

    public void setStage2Pass(Boolean stage2Pass) {
        this.stage2Pass = stage2Pass;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getComponentStatusJson() {
        return componentStatusJson;
    }

    public void setComponentStatusJson(String componentStatusJson) {
        this.componentStatusJson = componentStatusJson;
    }

    public String getMachineJson() {
        return machineJson;
    }

    public void setMachineJson(String machineJson) {
        this.machineJson = machineJson;
    }

    public String getRootCauseJson() {
        return rootCauseJson;
    }

    public void setRootCauseJson(String rootCauseJson) {
        this.rootCauseJson = rootCauseJson;
    }

    public String getLogsJson() {
        return logsJson;
    }

    public void setLogsJson(String logsJson) {
        this.logsJson = logsJson;
    }

    public String getRegistersAuditedJson() {
        return registersAuditedJson;
    }

    public void setRegistersAuditedJson(String registersAuditedJson) {
        this.registersAuditedJson = registersAuditedJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

