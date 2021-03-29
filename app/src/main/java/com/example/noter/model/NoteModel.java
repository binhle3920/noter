package com.example.noter.model;

import java.io.Serializable;

public class NoteModel implements Serializable {
    String note_title;
    String note_content;
    String note_date;
    String tag;

    public void setNoteModel(String note_title, String note_content, String note_date, String tag) {
        this.note_title = note_title;
        this.note_content = note_content;
        this.note_date = note_date;
        this.tag = tag;
    }

    public String getNoteTitle() {return note_title;}
    public String getNoteDate() {return note_date;}
    public String getNoteTag() {return tag;}
}
