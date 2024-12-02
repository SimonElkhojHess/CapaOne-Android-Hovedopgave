package com.example.capaoneandroidhovedopgave.service;
import com.example.capaoneandroidhovedopgave.Portaluser;

import java.io.IOException;

import okhttp3.*;
public class DeviceInfoService {
    public static void sendNewNameToDatabase(String jsonBody, String authToken) {
        OkHttpClient client = new OkHttpClient();

        Portaluser portaluserAuth = new Portaluser();
        String portaluserAuthToken = portaluserAuth.getPortaluserToken();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder().url("https://localhost:3300/api/organizations/1/android/enterprise/LC03g1yf5o/device/64365c56558de5ce65d85a82").header("X-Portaluser", portaluserAuthToken).post(body).build();

        client.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               e.printStackTrace();
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               if (response.isSuccessful()) {
                   response.body().string();
               } else {
                   System.err.println("Request failed with code: " + response.code());
               }
           }
        });
    }
}
