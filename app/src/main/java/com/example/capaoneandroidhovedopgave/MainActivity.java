package com.example.capaoneandroidhovedopgave;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Getting and filling the device name field for display info.
        TextView deviceNameField = findViewById(R.id.device_name_field);
        String deviceName = Settings.Global.getString(this.getContentResolver(), "device_name");
        deviceNameField.setText(deviceName);

        // Getting and filling the device OS version field for display info.
        TextView osVersionField = findViewById(R.id.os_version_field);
        String osVersion = android.os.Build.VERSION.RELEASE;
        osVersionField.setText(osVersion);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}