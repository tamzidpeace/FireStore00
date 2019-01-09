package com.example.arafat.firestore00;

public class Notes {

    private String title;
    private String description;

    public Notes() {
        // empty public constructor
    }

    public Notes(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
