package com.corylab.citatum.presentation.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.corylab.citatum.data.entity.EntityQuote;
import com.corylab.citatum.data.entity.QuoteTagJoin;
import com.corylab.citatum.data.model.Quote;
import com.corylab.citatum.data.repository.Repository;

import java.util.List;

public class QuoteViewModel extends AndroidViewModel {
    private Repository repository;
    private final LiveData<List<Quote>> quotes;

    public QuoteViewModel(Application application) {
        super(application);
        repository = new Repository(application);
        quotes = repository.getQuotes();
    }

    public LiveData<List<Quote>> getQuotes() {
        return quotes;
    }

    public void insert(Quote quote) {
        repository.insertQuote(quote);
    }

    public void delete(Quote quote) {
        repository.deleteQuote(quote);
    }

    public void update(Quote quote) {
        repository.updateQuote(quote);
    }

    public int getMaxId() {
        return repository.getQuoteMaxId();
    }

    public Quote getQuoteByUid(int uid) {
        return repository.getQuoteByUid(uid);
    }

    public LiveData<List<Quote>> getBookmarkedQuotes() {
       return repository.getBookmarkedQuotes();
    }

    public LiveData<List<Quote>> getRemovedQuotes() {
        return repository.getRemovedQuotes();
    }

    public LiveData<List<Quote>> getAllActive() {
        return repository.getAllActive();
    }
}
