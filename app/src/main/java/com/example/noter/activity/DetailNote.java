package com.example.noter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.AREditor;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.example.noter.R;
import com.example.noter.model.NoteModel;
import com.example.noter.model.TagModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DetailNote extends AppCompatActivity {
    TextView note_day;
    EditText note_title;
    TagModel tag;
    NoteModel note;

    //Input note framework
    private AREditor note_content;

    Spinner note_tag;
    List<TagModel> listTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listTag = (List<TagModel>) getIntent().getSerializableExtra("ListTag");
        setUpSpinner();
    }

    private void setUpRecourse() {
        note_day = findViewById(R.id.note_date);

        note_content = findViewById(R.id.are_editor);
        note_content.setExpandMode(AREditor.ExpandMode.FULL);
        note_content.setHideToolbar(false);
        note_content.setToolbarAlignment(AREditor.ToolbarAlignment.BOTTOM);
    };
    private void setUpSpinner() {
        List<String> list_tag = new ArrayList<>();

        for (TagModel item : listTag) {
            list_tag.add(item.getName());
        }

        note_tag = (Spinner) findViewById(R.id.note_tag);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.spinner_item, list_tag);
        note_tag.setAdapter(adp);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}