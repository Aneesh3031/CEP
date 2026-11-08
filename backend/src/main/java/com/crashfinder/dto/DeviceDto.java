package com.crashfinder.dto;

import java.util.Map;

public class DeviceDto {
    private String id;
    private String name;
    private boolean here;
    private Map<String, Object> info;
    private Map<String, Object> settings;

    public DeviceDto() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHere() {
        return here;
    }

    public void setHere(boolean here) {
        this.here = here;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }
}

