package com.example.capaoneandroidhovedopgave.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capaoneandroidhovedopgave.R;
import com.example.capaoneandroidhovedopgave.model.DeviceInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        DeviceInfo currentDevice = new DeviceInfo(this);

        // Inserting the device name into the TextView
        TextView deviceNameField = findViewById(R.id.device_name_field);
        String deviceName = currentDevice.getDeviceName();
        deviceNameField.setText(deviceName);

        boolean successfulNameChange = currentDevice.setDeviceName("new Device Name");
        if (successfulNameChange) {
            Toast.makeText(this, "Device name updated through app.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "An error occurred and device name was not changed.", Toast.LENGTH_SHORT).show();
        }


        // Getting and filling the device OS version field for display info.
        TextView osVersionField = findViewById(R.id.os_version_field);
        String osVersion = currentDevice.getOsVersion();
        osVersionField.setText(osVersion);

        // Getting and filling the device model field for the display info.
        TextView deviceModelField = findViewById(R.id.device_model_field);
        String deviceModel = currentDevice.getDeviceModel();
        deviceModelField.setText(deviceModel);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}