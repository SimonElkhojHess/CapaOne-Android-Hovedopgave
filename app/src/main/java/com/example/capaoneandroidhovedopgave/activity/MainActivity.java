package com.example.capaoneandroidhovedopgave.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capaoneandroidhovedopgave.R;
import com.example.capaoneandroidhovedopgave.model.DeviceInfo;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    ConstraintLayout constraintLayoutOriginal;
    ConstraintSet constraintSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.device_name);
        constraintLayoutOriginal = findViewById(R.id.device_name);
        constraintSet = new ConstraintSet();


        constraintSet.clone(constraintLayout);

        DeviceInfo currentDevice = new DeviceInfo(this);

        // Inserting the device name into the TextView
        TextView deviceNameField = findViewById(R.id.device_name_field);
        String deviceName = currentDevice.getDeviceName();
        deviceNameField.setText(deviceName);
        EditText editDeviceNameField = findViewById(R.id.edit_device_name_field);
        TextView deviceNameLabel = findViewById(R.id.device_name_label);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);



        Button deviceNameEditButton = findViewById(R.id.device_name_edit_button);
        deviceNameEditButton.setOnClickListener(v -> {
            /*deviceNameField.setHeight(0);
            deviceNameField.setWidth(0);
            editDeviceNameField.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));*/
            constraintSet.setVisibility(deviceNameField.getId(), View.GONE);
            constraintSet.setVisibility(editDeviceNameField.getId(), View.VISIBLE);
            constraintSet.connect(editDeviceNameField.getId(), ConstraintSet.END, deviceNameEditButton.getId(), ConstraintSet.START);
            constraintSet.connect(deviceNameEditButton.getId(), ConstraintSet.START, deviceNameLabel.getId(), ConstraintSet.END);
            constraintSet.applyTo(constraintLayout);
            editDeviceNameField.requestFocus();
            imm.showSoftInput(editDeviceNameField, InputMethodManager.SHOW_FORCED);
        });

        editDeviceNameField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                constraintSet.setVisibility(editDeviceNameField.getId(), View.GONE);
                constraintSet.setVisibility(deviceNameField.getId(), View.VISIBLE);
                constraintSet.connect(deviceNameEditButton.getId(), ConstraintSet.END, deviceNameField.getId(), ConstraintSet.START);
                constraintSet.applyTo(constraintLayout);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}