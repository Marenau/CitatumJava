package com.corylab.citatum.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.corylab.citatum.data.entity.EntityQuote;

import java.util.List;

@Dao
public interface QuoteDao {
    @Insert
    void insertAll(EntityQuote... quote);

    @Update
    void update(EntityQuote quote);

    @Delete
    void delete(EntityQuote quote);

    @Query("SELECT * FROM quotes_table ORDER BY uid")
    LiveData<List<EntityQuote>> getAll();

    @Query("SELECT MAX(uid) FROM quotes_table")
    int getMaxId();

    @Query("SELECT * FROM quotes_table WHERE uid = :id")
    EntityQuote getQuoteById(int id);
}