package com.example.noter.model;

public class NoteModel {
    String note_title;
    String note_content;
    String note_date;
    TagModel tag;

    public void NoteModel(String note_title, String note_content, String note_date, TagModel tag) {
        this.note_title = note_title;
        this.note_content = note_content;
        this.note_date = note_date;
        this.tag = tag;
    }
}
