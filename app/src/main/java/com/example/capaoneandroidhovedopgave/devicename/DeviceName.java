package com.example.capaoneandroidhovedopgave.devicename;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capaoneandroidhovedopgave.R;

public class DeviceName extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Getting and filling the device name field for display info.
        TextView deviceNameField = findViewById(R.id.device_name_field);
        String deviceName = Settings.Global.getString(this.getContentResolver(), "device_name");
        deviceNameField.setText(deviceName);

    }


}
