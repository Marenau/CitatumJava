package com.corylab.citatum.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.corylab.citatum.data.dao.QuoteDao;
import com.corylab.citatum.data.dao.TagDao;
import com.corylab.citatum.data.database.QuoteRoomDatabase;
import com.corylab.citatum.data.entity.EntityQuote;
import com.corylab.citatum.data.entity.EntityTag;
import com.corylab.citatum.data.model.Quote;
import com.corylab.citatum.data.model.Tag;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Repository {
    private QuoteDao quoteDao;
    private TagDao tagDao;
    private LiveData<List<Quote>> quotes;
    private LiveData<List<Tag>> tags;

    public Repository(Application application) {
        QuoteRoomDatabase quoteDB = QuoteRoomDatabase.getDatabase(application);
        quoteDao = quoteDB.quoteDao();
        tagDao = quoteDB.tagDao();
        quotes = Transformations.map(quoteDao.getAll(), entities -> entities.stream().map(EntityQuote::toQuote).collect(Collectors.toList()));
        tags = Transformations.map(tagDao.getAll(), entities -> entities.stream().map(EntityTag::toTag).collect(Collectors.toList()));
    }

    public LiveData<List<Quote>> getQuotes() {
        return quotes;
    }

    public LiveData<List<Tag>> getTags() {
        return tags;
    }

    public void insertQuote(EntityQuote quote) {
        QuoteRoomDatabase.databaseWriteExecutor.execute(() -> quoteDao.insertAll(quote));
    }

    public void deleteQuote(EntityQuote quote) {
        QuoteRoomDatabase.databaseWriteExecutor.execute(() -> quoteDao.delete(quote));
    }

    public void updateQuote(EntityQuote quote) {
        QuoteRoomDatabase.databaseWriteExecutor.execute(() -> quoteDao.update(quote));
    }

    public int getMaxId() {
        try {
            return QuoteRoomDatabase.databaseReadExecutor.submit(() -> quoteDao.getMaxId()).get();
        } catch (ExecutionException | InterruptedException e) {
            return 0;
        }
    }

    public EntityQuote getQuoteByUid(int uid) {
        try {
            return QuoteRoomDatabase.databaseReadExecutor.submit(() -> quoteDao.getQuoteById(uid)).get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    public void insertTag(EntityTag tag) {
        QuoteRoomDatabase.databaseWriteExecutor.execute(() -> tagDao.insertAll(tag));
    }

    public void deleteTag(EntityTag tag) {
        QuoteRoomDatabase.databaseWriteExecutor.execute(() -> tagDao.delete(tag));
    }

    public void updateTag(EntityTag tag) {
        QuoteRoomDatabase.databaseWriteExecutor.execute(() -> tagDao.update(tag));
    }
}
