package com.corylab.citatum.presentation.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.corylab.citatum.data.entity.EntityQuote;
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
        repository.insertQuote(toEntity(quote));
    }

    public void delete(Quote quote) {
        EntityQuote temp = toEntity(quote);
        temp.uid = quote.getUid();
        temp.bookmarkFlag = quote.getBookmarkFlag();
        repository.deleteQuote(temp);
    }

    public void update(Quote quote) {
        EntityQuote temp = toEntity(quote);
        temp.uid = quote.getUid();
        repository.updateQuote(temp);
    }

    public int getMaxId() {
        return repository.getMaxId();
    }

    public Quote getQuoteByUid(int uid) {
        return repository.getQuoteByUid(uid).toQuote();
    }

    private EntityQuote toEntity(Quote quote) {
        return new EntityQuote(quote.getTitle(), quote.getAuthor(), quote.getText(), quote.getDate(), quote.getPageNumber(), quote.getBookmarkFlag());
    }
}
