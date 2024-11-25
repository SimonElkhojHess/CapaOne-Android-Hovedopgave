package com.example.capaoneandroidhovedopgave.model;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

public class DeviceInfo {
    private Context context;
    private String deviceName;
    private String osVersion;
    private String deviceModel;

    // Constructor
    public DeviceInfo(Context context) {
        this.context = context;
    }

    // Getters
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

    // Setter
    public boolean setDeviceName(String newName) {
        return setDeviceNameInSystem(newName);
    }

    private boolean setDeviceNameInSystem(String newNameInSystem) {
        boolean wasNameChangedInSystem = false;

        try {
            wasNameChangedInSystem = Settings.Global.putString(context.getContentResolver(), "device_name", newNameInSystem);
        } catch(SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wasNameChangedInSystem;
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
