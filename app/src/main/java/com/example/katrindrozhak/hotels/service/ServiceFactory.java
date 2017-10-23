package com.example.katrindrozhak.hotels.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    /**
     * This is static method for creating service with retrofit
     *
     * @param clazz   is a parameter for API
     * @param baseUrl it is a base url of remote API
     * @param <T>     used to specify particular type
     * @return retrofit service
     */
    public static <T> T createRetrofitService(final Class<T> clazz, final String baseUrl) {

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(clazz);
    }
}
