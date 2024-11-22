package com.example.capaoneandroidhovedopgave.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.example.capaoneandroidhovedopgave.model.DeviceLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService {

    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;

    public LocationService(Context context) {
        this.context = context.getApplicationContext();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public interface LocationCallback {
        void onLocationResult(DeviceLocation deviceLocation);
        void onFailure(Exception e);
    }

    public void getDeviceLocation(LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onFailure(new SecurityException("Location permission not granted"));
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
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
            } else {
                callback.onFailure(new Exception("Unable to obtain location"));
            }
        }).addOnFailureListener(callback::onFailure);
    }
}