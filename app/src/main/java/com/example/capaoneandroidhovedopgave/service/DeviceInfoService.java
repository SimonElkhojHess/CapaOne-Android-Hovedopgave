package com.example.capaoneandroidhovedopgave.service;
import android.util.Log;

import com.example.capaoneandroidhovedopgave.model.DeviceRestrictions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;
public class DeviceInfoService {
    public static final OkHttpClient client = new OkHttpClient();
    public interface DeviceNameCallback {
        void onDeviceNameFetched(String deviceName);
        void onError(Exception e);
    }

    static DeviceRestrictions deviceRestrictions = new DeviceRestrictions();

    public static void setDeviceRestrictionsForApi(DeviceRestrictions deviceRestrictionsForApi) {
        deviceRestrictions.setAuthToken(deviceRestrictionsForApi.getAuthToken());
        deviceRestrictions.setOrgId(deviceRestrictionsForApi.getOrgId());
        deviceRestrictions.setEnterpriseId(deviceRestrictionsForApi.getEnterpriseId());
        deviceRestrictions.setDeviceId(deviceRestrictionsForApi.getDeviceId());
    }

    public static void sendBodyToDatabase(String jsonBody) {

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder().url("http://:3300/api/organizations/" + deviceRestrictions.getOrgId() + "/android/enterprise/" + deviceRestrictions.getEnterpriseId() + "/device/" + deviceRestrictions.getDeviceId()).header("X-Portaluser", deviceRestrictions.getAuthToken()).put(body).build();

        client.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               e.printStackTrace();
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               if (response.isSuccessful()) {
                   String responseBody = response.body().string();
                   Log.d("DeviceInfoService", "Response: " + responseBody);
               } else {
                   String errorBody = response.body().string();
                   Log.e("DeviceInfoService", "Error: " + errorBody);
               }
           }
        });
    }

    public static void getDeviceNameFromDatabase(DeviceNameCallback callback) {

        Request request = new Request.Builder().url("http://10.0.2.2:3300/api/organizations/" + deviceRestrictions.getOrgId() + "/android/enterprise/" + deviceRestrictions.getEnterpriseId() + "/device/" + deviceRestrictions.getDeviceId()).header("X-Portaluser", deviceRestrictions.getAuthToken()).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("DeviceInfoService", "Response from Get: " + responseBody);

                    String deviceName = parseDeviceName(responseBody);

                    Log.d("DeviceInfoService", "This is the deviceName variable: " + deviceName);
                    callback.onDeviceNameFetched(deviceName);
                } else {
                    String errorBody = response.body().string();
                    Log.e("DeviceInfoService", "Error: " + errorBody);
                    callback.onError(new Exception("Request not succesful: " + errorBody));
                }
            }
        });
    }

    private static String parseDeviceName(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            return jsonObject.getString("capaoneDeviceName");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
