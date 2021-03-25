package com.example.noter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.noter.R;
import com.google.gson.Gson;

public class ListNote extends AppCompatActivity {
    ImageButton add_note;
    SharedPreferences sharedPreferences; //loading and saving notes/tags
    SharedPreferences.Editor editor;
    Gson gson = new Gson();

    private static final String PREF_TAG = "com.example.noter.PREFERENCES";
    private static final String TAG = "Tags";
    private static final String NOTE = "Notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);

        setUpResources();
    }

    private void setUpResources() {
        //Set up button
        add_note = (ImageButton) findViewById(R.id.add_note);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListNote.this, DetailNote.class);
                startActivity(intent);
            }
        });

        //SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //Create search button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        inflater.inflate(R.menu.menu, menu);

        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQuery(null, false);
        searchView.setQueryHint("Search tag or title of notes ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}