package com.example.katrindrozhak.hotels.service;

import com.example.katrindrozhak.hotels.model.Hotel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HotelApi {

    @GET("/iMofas/ios-android-test/master/0777.json")
    Observable<List<Hotel>> getHotelData();

    @GET("/iMofas/ios-android-test/master/{id}.json")
    Observable<Hotel> getHotel(@Path("id") Integer id);
}
