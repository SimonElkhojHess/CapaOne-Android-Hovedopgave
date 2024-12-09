package com.example.capaoneandroidhovedopgave.model;

public class DeviceLocation {

    private String country;
    private String region;
    private String city;
    private String street;
    private String streetNumber;
    private double latitude;
    private double longitude;

    public DeviceLocation(String country, String region, String city, String street, String streetNumber, double latitude, double longitude) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DeviceLocation() {}

    public String getCountry() { return country; }
    public String getRegion() { return region; }
    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getStreetNumber() { return streetNumber; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

}
