package com.crashfinder.dto;

import java.util.List;
import java.util.Map;

public class ReportDto {
    private String id;
    private String date;
    private String result;
    private String severity;
    private String title;
    private String msg;
    private String fix;
    private String comp;
    private Integer score;
    private Boolean deep;
    private Boolean stage1Pass;
    private Boolean stage2Pass;
    private String scenario;
    private String deviceId;
    private String deviceName;
    private Map<String, Object> machine;
    private Map<String, Object> rootCause;
    private Map<String, Object> st;
    private List<Map<String, Object>> logs;
    private List<String> registersAudited;

    public ReportDto() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getDeep() {
        return deep;
    }

    public void setDeep(Boolean deep) {
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Map<String, Object> getMachine() {
        return machine;
    }

    public void setMachine(Map<String, Object> machine) {
        this.machine = machine;
    }

    public Map<String, Object> getRootCause() {
        return rootCause;
    }

    public void setRootCause(Map<String, Object> rootCause) {
        this.rootCause = rootCause;
    }

    public Map<String, Object> getSt() {
        return st;
    }

    public void setSt(Map<String, Object> st) {
        this.st = st;
    }

    public List<Map<String, Object>> getLogs() {
        return logs;
    }

    public void setLogs(List<Map<String, Object>> logs) {
        this.logs = logs;
    }

    public List<String> getRegistersAudited() {
        return registersAudited;
    }

    public void setRegistersAudited(List<String> registersAudited) {
        this.registersAudited = registersAudited;
    }
}

