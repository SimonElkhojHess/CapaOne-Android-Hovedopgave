package com.example.capaoneandroidhovedopgave.model;



import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
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
        SharedPreferences sharedPreferences = context.getSharedPreferences("DeviceInfo", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("device_name", "Default Device Name").equals("Default Device Name")) {
            deviceName = Settings.Global.getString(context.getContentResolver(), "device_name");
        } else {
            deviceName = sharedPreferences.getString("device_name", "Default Device Name");
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
    public void setDeviceName(String newName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DeviceInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("device_name", newName);
        editor.apply();
        this.deviceName = sharedPreferences.getString("device_name", "Default Device Name");
    }

}
