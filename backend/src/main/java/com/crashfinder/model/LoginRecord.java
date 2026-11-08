package com.crashfinder.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_records")
public class LoginRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String email;

    @Column(nullable = false)
    private LocalDateTime loginTime = LocalDateTime.now();

    @Column(nullable = false)
    private boolean success;

    @Column(length = 100)
    private String ipAddress;

    @Column(length = 300)
    private String userAgent;

    @Column(length = 255)
    private String failureReason;

    public LoginRecord() {}

    public LoginRecord(String email, boolean success, String ipAddress, String userAgent, String failureReason) {
        this.email = email;
        this.success = success;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.failureReason = failureReason;
        this.loginTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}

