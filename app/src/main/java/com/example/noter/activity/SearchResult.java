package com.example.noter.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.noter.R;
import com.example.noter.adapter.NoteAdapter;
import com.example.noter.adapter.ResultAdapter;
import com.example.noter.model.NoteModel;
import com.example.noter.model.TagModel;

import java.util.List;

public class SearchResult extends AppCompatActivity {
    RecyclerView resultView;
    LinearLayout emptyLayout;

    List<TagModel> listTag;
    List<NoteModel> listNote, resultNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get intent
        listTag = (List<TagModel>) getIntent().getSerializableExtra("ListTag");
        listNote = (List<NoteModel>) getIntent().getSerializableExtra("ListNote");
        resultNote = (List<NoteModel>) getIntent().getSerializableExtra("SearchResult");

        //find view
        resultView = findViewById(R.id.result_view);
        emptyLayout = findViewById(R.id.empty_layout);

        //setup adapter
        if (resultNote.isEmpty()) {
            resultView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            resultView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            ResultAdapter noteAdapter = new ResultAdapter(this, R.id.result_view, resultNote, listNote, listTag);
            resultView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            resultView.setAdapter(noteAdapter);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}