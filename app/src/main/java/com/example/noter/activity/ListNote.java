package com.example.noter.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.noter.R;
import com.example.noter.adapter.TagAdapter;
import com.example.noter.model.NoteModel;
import com.example.noter.model.TagModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

public class ListNote extends AppCompatActivity {
    ImageButton add_note;
    List<TagModel> listTag;
    List<NoteModel> listNote;
    RecyclerView recyclerViewTag, recyclerViewNote;
    SharedPreferences sharedPreferences; //loading and saving notes/tags
    SharedPreferences.Editor editor;
    Gson gson;
    String current_color = String.valueOf(R.color.black);


    private static final String PREF_TAG = "com.example.noter.PREFERENCES";
    private static final String TAG = "Tags";
    private static final String NOTE = "Notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);

        setUpResources();

        //set tag adapter
        TagAdapter tagAdapter = new TagAdapter(this, R.id.list_tag, listTag, listNote);
        recyclerViewTag.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTag.setAdapter(tagAdapter);

        //set note adapter
        //NoteAdapter noteAdapter = new NoteAdapter(this, R.id.list_note, listNote);
        //recyclerViewNote.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerViewNote.setAdapter(noteAdapter);
    }

    private void setUpResources() {
        //Set up button
        add_note = (ImageButton) findViewById(R.id.add_note);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListNote.this, DetailNote.class);
                intent.putExtra("ListTag", (Serializable) listTag);
                intent.putExtra("ListNote", (Serializable) listNote);
                finish();
                startActivity(intent);
            }
        });

        //SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();

        //get list tag
        getListTag();

        //get list note
        getListNote();

        //Find recycler view
        recyclerViewTag = findViewById(R.id.list_tag);
    }

    private void getListTag() {
        //Get all tags
        listTag = getTagList();
        if (listTag == null) {
            listTag = new ArrayList<>();
            TagModel defaultTag = new TagModel();
            defaultTag.setTagModel("None Tag".toUpperCase(), String.valueOf(Color.parseColor("#222426")));
            listTag.add(defaultTag);
        }
    }

    private void getListNote() {
        //Get all tags
        listNote = getNoteList();
        if (listNote == null) {
            listNote = new ArrayList<>();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.btn_add_tag:
                createAddTagDialog();
                return true;
            case R.id.btn_rm_tag:
                return true;
            case R.id.btn_edit_tag:
                return true;
        }

        return false;
    }

    public List<TagModel> getTagList() {
        List<TagModel> arrayItems = null;
        String serializedObject = sharedPreferences.getString(TAG, null);
        if (serializedObject != null) {
            gson = new Gson();
            Type type = new TypeToken<List<TagModel>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }

    public List<NoteModel> getNoteList() {
        List<NoteModel> arrayItems = null;
        String serializedObject = sharedPreferences.getString(NOTE, null);
        if (serializedObject != null) {
            gson = new Gson();
            Type type = new TypeToken<List<NoteModel>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }

    public void createAddTagDialog() {
        AlertDialog.Builder addDialogBuilder;
        AlertDialog addDialog;

        addDialogBuilder = new AlertDialog.Builder(this);
        final View addTagView = getLayoutInflater().inflate(R.layout.add_tag_dialog, null);

        addDialogBuilder.setView(addTagView);
        addDialog = addDialogBuilder.create();
        addDialog.show();

        EditText tag_name = (EditText) addTagView.findViewById(R.id.tag_name);
        Button pick_color = (Button) addTagView.findViewById(R.id.btn_tag_color);
        Button save = (Button) addTagView.findViewById(R.id.btn_save);


        pick_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker colorPicker = new ColorPicker(ListNote.this);
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        pick_color.setBackgroundColor(color);
                        current_color = String.valueOf(color);
                    }

                    @Override
                    public void onCancel(){
                        // put code
                    }
                });
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag_name_string = tag_name.getText().toString().toUpperCase();

                //check if tag name is exist
                if (listTag != null) {
                    for (TagModel tag : listTag) {
                        if (tag_name_string.equals(tag.getName())) {
                            Toast.makeText(ListNote.this, "Tag name existed! Choose another one.", Toast.LENGTH_SHORT).show();
                            addDialog.cancel();
                            return;
                        }
                    }
                } else {
                    listTag = new ArrayList<>();
                }

                //if not existed, insert a new tag
                TagModel tag = new TagModel();
                tag.setTagModel(tag_name_string, current_color);
                listTag.add(tag);
                saveTag(TAG, gson.toJson(listTag));
                Toast.makeText(ListNote.this, "Tag added", Toast.LENGTH_SHORT).show();
                addDialog.cancel();

                //refresh activity
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void saveTag(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveTag(TAG, gson.toJson(listTag));
    }
}