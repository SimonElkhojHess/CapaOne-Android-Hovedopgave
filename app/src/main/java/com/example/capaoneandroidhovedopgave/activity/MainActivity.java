package com.example.capaoneandroidhovedopgave.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
        deviceNameField.setText(currentDevice.getDeviceName());
        EditText editDeviceNameField = findViewById(R.id.edit_device_name_field);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);



        Button deviceNameEditButton = findViewById(R.id.device_name_edit_button);
        Button approveNewDeviceName = findViewById(R.id.approve_new_device_name);

        deviceNameEditButton.setOnClickListener(v -> {
            deviceNameField.setVisibility(View.GONE);
            deviceNameEditButton.setVisibility(View.GONE);
            editDeviceNameField.setVisibility(View.VISIBLE);
            approveNewDeviceName.setVisibility(View.VISIBLE);
            editDeviceNameField.requestFocus();
            imm.showSoftInput(editDeviceNameField, InputMethodManager.SHOW_FORCED);
        });

        approveNewDeviceName.setOnClickListener(v -> {
            String newDeviceName = editDeviceNameField.getText().toString();
            editDeviceNameField.setVisibility(View.GONE);
            approveNewDeviceName.setVisibility(View.GONE);
            deviceNameField.setVisibility(View.VISIBLE);
            deviceNameEditButton.setVisibility(View.VISIBLE);

            boolean successfulNameChange = currentDevice.setDeviceName(newDeviceName);
            if (successfulNameChange) {
                Toast.makeText(this, "Device name updated through app.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "An error occurred and device name was not changed.", Toast.LENGTH_SHORT).show();
            }
            if (successfulNameChange) {
                deviceNameField.setText(currentDevice.getDeviceName());
            }
        });

        editDeviceNameField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                editDeviceNameField.setVisibility(View.GONE);
                approveNewDeviceName.setVisibility(View.GONE);
                deviceNameField.setVisibility(View.VISIBLE);
                deviceNameEditButton.setVisibility(View.VISIBLE);
            }
        });




        /*boolean successfulNameChange = currentDevice.setDeviceName("new Device Name");
        if (successfulNameChange) {
            Toast.makeText(this, "Device name updated through app.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "An error occurred and device name was not changed.", Toast.LENGTH_SHORT).show();
        }*/


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

    // Method for closing the edit device name EditText, if clicking on anything other than the "Approve" button or the EditText field itself.
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            View b = findViewById(R.id.approve_new_device_name);
            if (v instanceof EditText) {
                Rect outRectText = new Rect();
                Rect outRectButton = new Rect();
                b.getGlobalVisibleRect(outRectButton);
                v.getGlobalVisibleRect(outRectText);
                if (!outRectText.contains((int) event.getRawX(), (int) event.getRawY()) && !outRectButton.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}