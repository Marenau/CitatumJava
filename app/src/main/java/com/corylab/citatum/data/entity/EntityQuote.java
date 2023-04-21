package com.corylab.citatum.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.corylab.citatum.data.model.Quote;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "quotes_table")
public class EntityQuote {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    public Integer uid = null;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "author")
    public String author;
    @ColumnInfo(name = "text")
    public String text;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "page_number")
    public String pageNumber;
    @ColumnInfo(name = "flag")
    public int bookmarkFlag;

    public EntityQuote(String title, String author, String text, String date, String pageNumber, int bookmarkFlag) {
        this.title = title;
        this.author = author;
        this.text = text;
        this.date = date;
        this.pageNumber = pageNumber;
        this.bookmarkFlag = bookmarkFlag;
    }

    public Quote toQuote() {
        return new Quote(uid, title, author, text, date, pageNumber, bookmarkFlag);
    }
}
