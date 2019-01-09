package com.example.arafat.firestore00;

import com.google.firebase.firestore.Exclude;

public class Notes {

    private String title;
    private String description;
    private String documentID;
    private int priority;

    public Notes() {
        // empty public constructor
    }

    public Notes(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
