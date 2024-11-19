package com.example.capaoneandroidhovedopgave.model;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

public class DeviceInfo {
    private Context context;
    private String deviceName;
    private String osVersion;
    private String deviceModel;

    public DeviceInfo(Context context) {
        this.context = context;
    }

    public String getDeviceName() {
        if (deviceName == null) {
            deviceName = retrieveDeviceName();
        }
        return deviceName;
    }

    public String getOsVersion() {
        osVersion = Build.VERSION.RELEASE;
        return osVersion;
    }

    public String getDeviceModel() {
        deviceModel = Build.MODEL;
        return deviceModel;
    }
    private String retrieveDeviceName() {
        String name = null;
        try {
            name = Settings.Global.getString(context.getContentResolver(), "device_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (name == null || name.isEmpty()) {
            name = "Device name not found!";
        }

        return name;

    }

}
