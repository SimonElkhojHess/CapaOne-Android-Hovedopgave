package com.example.capaoneandroidhovedopgave.model;

public class ApiLocationBody {
    private DeviceLocation deviceLocation;

    public ApiLocationBody(double latitude, double longitude) {
        this.deviceLocation = new DeviceLocation(latitude, longitude);
    }

    public DeviceLocation getDeviceLocation() {
        return deviceLocation;
    }
}
