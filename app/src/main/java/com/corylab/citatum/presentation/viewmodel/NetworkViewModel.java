package com.corylab.citatum.presentation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.corylab.citatum.data.repository.NetworkRepository;
import com.corylab.citatum.network.ForismaticQuote;

public class NetworkViewModel extends AndroidViewModel {

    private final NetworkRepository repository;

    public NetworkViewModel(@NonNull Application application) {
        super(application);
        repository = new NetworkRepository();
    }

    public ForismaticQuote getQuote() {
        return repository.getQuote();
    }
}
