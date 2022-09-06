package com.gmail.monajemi.am.android.qrscanner.model;

public class History {
    private int id;
    String link;
    String date;

    public History(int id, String link, String date) {
        this.id = id;
        this.link = link;
        this.date = date;
    }
    public History( String link, String date) {
        this.link = link;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }
}
