package com.example.noter.model;

import java.util.List;

public class TagModel {
    String tag_name;
    String hex_color;
    List<NoteModel> list_note;

    public void TagModel(String tag_name, String hex_color, List<NoteModel> list_note) {
        this.tag_name = tag_name;
        this.hex_color = hex_color;
        this.list_note = list_note;
    }
}
