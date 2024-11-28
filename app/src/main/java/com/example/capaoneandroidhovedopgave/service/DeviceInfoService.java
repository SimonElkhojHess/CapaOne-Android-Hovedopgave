package com.example.capaoneandroidhovedopgave.service;
import okhttp3.*;
public class DeviceInfoService {
    public void sendNewNameToDatabase(String jsonBody, String authToken) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder().url("https://")
    }
}
