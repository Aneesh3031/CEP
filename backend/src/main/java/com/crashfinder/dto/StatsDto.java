package com.crashfinder.dto;

import java.util.List;
import java.util.Map;

public class StatsDto {
    private long totalScans;
    private long totalLogins;
    private long successfulLogins;
    private long failedLogins;
    private long totalDevices;
    private long totalFaults;
    private double averageHealthScore;
    private List<Map<String, Object>> recentLogins;
    private List<Map<String, Object>> recentReports;

    public StatsDto() {}

    public long getTotalScans() {
        return totalScans;
    }

    public void setTotalScans(long totalScans) {
        this.totalScans = totalScans;
    }

    public long getTotalLogins() {
        return totalLogins;
    }

    public void setTotalLogins(long totalLogins) {
        this.totalLogins = totalLogins;
    }

    public long getSuccessfulLogins() {
        return successfulLogins;
    }

    public void setSuccessfulLogins(long successfulLogins) {
        this.successfulLogins = successfulLogins;
    }

    public long getFailedLogins() {
        return failedLogins;
    }

    public void setFailedLogins(long failedLogins) {
        this.failedLogins = failedLogins;
    }

    public long getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(long totalDevices) {
        this.totalDevices = totalDevices;
    }

    public long getTotalFaults() {
        return totalFaults;
    }

    public void setTotalFaults(long totalFaults) {
        this.totalFaults = totalFaults;
    }

    public double getAverageHealthScore() {
        return averageHealthScore;
    }

    public void setAverageHealthScore(double averageHealthScore) {
        this.averageHealthScore = averageHealthScore;
    }

    public List<Map<String, Object>> getRecentLogins() {
        return recentLogins;
    }

    public void setRecentLogins(List<Map<String, Object>> recentLogins) {
        this.recentLogins = recentLogins;
    }

    public List<Map<String, Object>> getRecentReports() {
        return recentReports;
    }

    public void setRecentReports(List<Map<String, Object>> recentReports) {
        this.recentReports = recentReports;
    }
}

