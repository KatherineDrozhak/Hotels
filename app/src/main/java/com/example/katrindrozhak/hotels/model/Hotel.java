package com.example.katrindrozhak.hotels.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Arrays;
import java.util.List;

@Parcel
public class Hotel {

    private Integer id;
    private String name;
    private String address;
    private Float stars;
    private Float distance;
    private String image;
    private Float lat;
    private Float lon;

    @SerializedName("suites_availability")
    @Expose
    private String suitesAvailability;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Float getStars() {
        return stars;
    }

    public Float getDistance() {
        return distance;
    }

    public Integer getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public Float getLat() {
        return lat;
    }

    public Float getLon() {
        return lon;
    }

    public List<String> getAvailableRooms() {
        return Arrays.asList(suitesAvailability.split(":"));
    }

    public String getAvailableRoomsInfo() {
        return TextUtils.join(", ", getAvailableRooms());
    }
}
