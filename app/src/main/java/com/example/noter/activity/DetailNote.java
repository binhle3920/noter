package com.example.noter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.noter.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DetailNote extends AppCompatActivity {
    //TextView note_day;
    //EditText note_content, note_title;
    Spinner note_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpSpinner();
    }

    private void setUpSpinner() {
        List<String> list_tag = new ArrayList<>();

        //this is only for testing
        list_tag.add("Non-tag");
        list_tag.add("Study");
        list_tag.add("Activity");
        //the codes above are provided for testing purpose only

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