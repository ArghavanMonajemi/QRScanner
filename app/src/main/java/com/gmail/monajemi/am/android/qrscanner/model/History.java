package com.gmail.monajemi.am.android.qrscanner.model;

public class History {
    private int id;
    String data;
    String date;

    public History(int id, String data, String date) {
        this.id = id;
        this.data = data;
        this.date = date;
    }
    public History(String data, String date) {
        this.data = data;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getDate() {
        return date;
    }
}
