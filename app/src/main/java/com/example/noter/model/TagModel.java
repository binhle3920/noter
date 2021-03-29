package com.example.noter.model;

import java.io.Serializable;
import java.util.List;

public class TagModel implements Serializable {
    String tag_name;
    String hex_color;
    List<NoteModel> list_note;

    public void setTagModel(String tag_name, String hex_color, List<NoteModel> list_note) {
        this.tag_name = tag_name;
        this.hex_color = hex_color;
        this.list_note = list_note;
    }

    public String getName() {return tag_name;}
    public int getColor() {return Integer.parseInt(hex_color);}
}
