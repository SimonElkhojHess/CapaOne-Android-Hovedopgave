package com.example.capaoneandroidhovedopgave.model;

public class DeviceRestrictions {
    private String authToken;
    private String orgId;
    private String enterpriseId;
    private String deviceId;

    // Constructor
    public DeviceRestrictions(String authToken, String orgId, String enterpriseId, String deviceId) {
        this.authToken = authToken;
        this.orgId = orgId;
        this.enterpriseId = enterpriseId;
        this.deviceId = deviceId;
    }

    public DeviceRestrictions() {}

    // Getters
    public String getAuthToken() {
        return authToken;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    // Setters

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
