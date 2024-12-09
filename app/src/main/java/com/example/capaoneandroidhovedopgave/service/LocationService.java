package com.example.capaoneandroidhovedopgave.service;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.RestrictionsManager;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.capaoneandroidhovedopgave.Portaluser;
import com.example.capaoneandroidhovedopgave.model.ApiLocationBody;
import com.example.capaoneandroidhovedopgave.model.DeviceLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LocationService {

    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    Gson gson = new Gson();

    String authToken = "";

    public LocationService(Context context) {
        this.context = context.getApplicationContext();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }

    public void fetchLocation(Activity activity, String authTokenForApi, int requestCode, DeviceLocationCallback callback) {
        if(!isLocationPermissionGranted()) {
            requestLocationPermission(activity, requestCode);
            callback.onFailure(new SecurityException("Location permission not granted"));
            return;
        }
        authToken = authTokenForApi;
        getDeviceLocation(callback);
        startLocationUpdates(callback);
    }


    public interface DeviceLocationCallback {
        void onLocationResult(DeviceLocation deviceLocation);
        void onFailure(Exception e);
    }

    public void getDeviceLocation(DeviceLocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onFailure(new SecurityException("Location permission not granted"));
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            processLocation(location, callback);
        }).addOnFailureListener(callback::onFailure);
    }

    private void processLocation(Location location, DeviceLocationCallback callback) {
        if (location == null) {
            callback.onFailure(new Exception("Location is null"));
            return;
        }

        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocation( location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    DeviceLocation deviceLocation = new DeviceLocation(
                            address.getCountryName(),
                            address.getAdminArea(),
                            address.getLocality(),
                            address.getThoroughfare(),
                            address.getSubThoroughfare(),
                            location.getLatitude(),
                            location.getLongitude()
                    );
                    sendDeviceLocationToDatabase(deviceLocation, authToken);
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onLocationResult(deviceLocation)
                    );
                } else {
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onFailure(new Exception("No address found"))
                    );
                }
            } catch (IOException e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        callback.onFailure(e)
                );
            }
        }).start();

    }

    private LocationRequest createLocationRequest() {
        LocationRequest.Builder locationRequest = new LocationRequest.Builder(3600000);
        locationRequest.setDurationMillis(Long.MAX_VALUE);
        locationRequest.setIntervalMillis(3600000);
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMinUpdateIntervalMillis(3599999);
        locationRequest.setMaxUpdateAgeMillis(4200000);
        return locationRequest.build();
    }

    private LocationCallback createLocationCallback(DeviceLocationCallback callback) {
        return new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location latestLocation = locationResult.getLastLocation();
                processLocation(latestLocation, callback);

            }
        };
    }

    private void startLocationUpdates(DeviceLocationCallback callback) {

        LocationRequest locationRequest = createLocationRequest();

        LocationCallback locationCallback = createLocationCallback(callback);

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (SecurityException securityException) {
            Log.d("LocationService", "Security Exception: " + securityException);
        }

    }

    private void sendDeviceLocationToDatabase(DeviceLocation deviceLocation, String authToken) {
        String deviceLocationString = deviceLocation.getStreet() + " " + deviceLocation.getStreetNumber() + " " + deviceLocation.getCity() + ", " + deviceLocation.getRegion() + " " + deviceLocation.getCountry() + "\n" + deviceLocation.getLatitude() + ", " + deviceLocation.getLongitude();

        ApiLocationBody apiBody = new ApiLocationBody(deviceLocationString);
        String jsonBody = gson.toJson(apiBody);
        Log.d("Device Location update", "Request body: " + jsonBody);
        DeviceInfoService.sendBodyToDatabase(jsonBody, authToken);
    }

}