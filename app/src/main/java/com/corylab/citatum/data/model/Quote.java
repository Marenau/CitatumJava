package com.corylab.citatum.data.model;

public class Quote {
    private int uid;
    private String title;
    private String author;
    private String text;
    private String date;
    private String pageNumber;
    private int bookmarkFlag;

    public Quote(int uid, String title, String author, String text, String date, String pageNumber, int bookmarkFlag) {
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.text = text;
        this.date = date;
        this.pageNumber = pageNumber;
        this.bookmarkFlag = bookmarkFlag;
    }

    public Quote(String title, String author, String text, String date, String pageNumber) {
        this.title = title;
        this.author = author;
        this.text = text;
        this.date = date;
        this.pageNumber = pageNumber;
    }

    public Quote() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getBookmarkFlag() {
        return bookmarkFlag;
    }

    public void setBookmarkFlag(int bookmarkFlag) {
        this.bookmarkFlag = bookmarkFlag;
    }
}