package com.corylab.citatum.data.repository;

import com.corylab.citatum.data.datasource.NetworkSource;
import com.corylab.citatum.network.ForismaticQuote;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class NetworkRepository {

    private final NetworkSource networkSource;

    public NetworkRepository() {
        networkSource = new NetworkSource();
    }

    public ForismaticQuote getQuote() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.submit(() -> {
                Call<ForismaticQuote> call = networkSource.getForismaticApi().getQuote("getQuote", "json", null, "ru");
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
