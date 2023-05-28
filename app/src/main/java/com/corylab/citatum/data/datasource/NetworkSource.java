package com.corylab.citatum.data.datasource;

import com.corylab.citatum.network.ForismaticApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkSource {

    private final ForismaticApi forismaticApi;

    public NetworkSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.forismatic.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        forismaticApi = retrofit.create(ForismaticApi.class);
    }

    public ForismaticApi getForismaticApi() {
        return forismaticApi;
    }
}
