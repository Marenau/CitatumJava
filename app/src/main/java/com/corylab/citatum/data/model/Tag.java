package com.corylab.citatum.data.model;

public class Tag {
    private int uid;
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public Tag(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}