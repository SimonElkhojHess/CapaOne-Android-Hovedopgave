package com.example.capaoneandroidhovedopgave.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.RestrictionsManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capaoneandroidhovedopgave.R;
import com.example.capaoneandroidhovedopgave.model.ApiBody;
import com.example.capaoneandroidhovedopgave.model.DeviceInfo;
import com.example.capaoneandroidhovedopgave.model.DeviceLocation;
import com.example.capaoneandroidhovedopgave.model.DeviceRestrictions;
import com.example.capaoneandroidhovedopgave.service.DeviceInfoService;
import com.example.capaoneandroidhovedopgave.service.LocationService;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private LocationService locationService;
    Gson gson = new Gson();
    private String authTokenForPermission = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        RestrictionsManager restrictionsManager = (RestrictionsManager) getSystemService(Context.RESTRICTIONS_SERVICE);
        Bundle appRestrictions = restrictionsManager.getApplicationRestrictions();
        String authToken = appRestrictions.getString("auth_token", "");
        String orgId = appRestrictions.getString("org_id", "");
        String enterpriseId = appRestrictions.getString("enterprise_id", "");
        String deviceId = appRestrictions.getString("device_id", "");
        authTokenForPermission = authToken;
        DeviceRestrictions deviceRestrictions = new DeviceRestrictions(authToken, orgId, enterpriseId, deviceId);
        DeviceInfoService.setDeviceRestrictionsForApi(deviceRestrictions);

        DeviceInfo currentDevice = new DeviceInfo(this);

        DeviceInfoService.getDeviceNameFromDatabase(new DeviceInfoService.DeviceNameCallback() {
            @Override
            public void onDeviceNameFetched(String deviceNameFromDatabase) {
                runOnUiThread(() -> {
                    if (deviceNameFromDatabase.isEmpty()) {
                        String deviceNameOnDevice = currentDevice.getDeviceName();
                        prepareDeviceNameForBody(deviceNameOnDevice);
                    } else {
                        currentDevice.setDeviceName(deviceNameFromDatabase);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        // Inserting the device name into the TextView
        TextView deviceNameField = findViewById(R.id.device_name_field);
        deviceNameField.setText(currentDevice.getDeviceName());
        EditText editDeviceNameField = findViewById(R.id.edit_device_name_field);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        // Getting buttons for the name change and setting listeners to give functionality to the buttons
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

            prepareDeviceNameForBody(newDeviceName);

            currentDevice.setDeviceName(newDeviceName);
            deviceNameField.setText(currentDevice.getDeviceName());
        });

        editDeviceNameField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                editDeviceNameField.setVisibility(View.GONE);
                approveNewDeviceName.setVisibility(View.GONE);
                deviceNameField.setVisibility(View.VISIBLE);
                deviceNameEditButton.setVisibility(View.VISIBLE);
            }
        });


        // Getting and filling the device OS version field for display info.
        TextView osVersionField = findViewById(R.id.os_version_field);
        String osVersion = currentDevice.getOsVersion();
        osVersionField.setText(osVersion);

        // Getting and filling the device model field for the display info.
        TextView deviceModelField = findViewById(R.id.device_model_field);
        String deviceModel = currentDevice.getDeviceModel();
        deviceModelField.setText(deviceModel);

        // Location
        locationService = new LocationService(this);
        checkAndFetchLocation(authToken);

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

    // Checking if location permission is granted then fetch it, and if not request it.
    private void checkAndFetchLocation(String authToken) {
        locationService.fetchLocation(this, authToken, LOCATION_PERMISSION_REQUEST_CODE, new LocationService.DeviceLocationCallback() {
            @Override
            public void onLocationResult(DeviceLocation deviceLocation) {
                updateUIWithLocation(deviceLocation);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this, "Error fetching location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkAndFetchLocation(authTokenForPermission);
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUIWithLocation(DeviceLocation deviceLocation) {
        TextView deviceLocationField = findViewById(R.id.device_location_field);
        String street = deviceLocation.getStreet();
        String streetNumber = deviceLocation.getStreetNumber();
        String city = deviceLocation.getCity();
        String region = deviceLocation.getRegion();
        String country = deviceLocation.getCountry();
        double longitude = deviceLocation.getLongitude();
        double latitude = deviceLocation.getLatitude();

        String deviceLocationStringForField = street + " " + streetNumber + " " + city + ", " + region + " " + country + "\n" + latitude + ", " + longitude;

        deviceLocationField.setText(deviceLocationStringForField);
    }

    private void prepareDeviceNameForBody(String newDeviceName) {
        ApiBody body = new ApiBody(newDeviceName);
        String jsonBody = gson.toJson(body);
        System.out.println(jsonBody);
        Log.d("DeviceInfoService", "Request body: " + jsonBody);
        DeviceInfoService.sendBodyToDatabase(jsonBody);
    }

}