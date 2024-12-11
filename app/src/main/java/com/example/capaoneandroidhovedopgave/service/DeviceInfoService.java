package com.example.capaoneandroidhovedopgave.service;
import android.util.Log;

import com.example.capaoneandroidhovedopgave.Portaluser;

import java.io.IOException;

import okhttp3.*;
public class DeviceInfoService {
    public static final OkHttpClient client = new OkHttpClient();
    public static void sendBodyToDatabase(String jsonBody, String authToken) {


        Portaluser portaluserAuth = new Portaluser();
        String portaluserAuthToken = portaluserAuth.getPortaluserToken();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder().url("http://10.0.2.2:3300/api/organizations/1/android/enterprise/LC03g1yf5o/device/64365c56558de5ce65d85a82").header("X-Portaluser", portaluserAuthToken).put(body).build();

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
}
