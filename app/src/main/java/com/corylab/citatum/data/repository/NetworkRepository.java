package com.corylab.citatum.data.repository;

import com.corylab.citatum.network.ForismaticApi;
import com.corylab.citatum.network.ForismaticQuote;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkRepository {

    private final ForismaticApi forismaticApi;

    public NetworkRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.forismatic.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        forismaticApi = retrofit.create(ForismaticApi.class);
    }

    public ForismaticQuote getQuote() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.submit(() -> {
                Call<ForismaticQuote> call = forismaticApi.getQuote("getQuote", "json", null, "ru");
                try {
                    Response<ForismaticQuote> response = call.execute();
                    if (response.isSuccessful()) {
                        ForismaticQuote forismaticResponse = response.body();
                        if (forismaticResponse != null) {
                            return forismaticResponse;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }
}
