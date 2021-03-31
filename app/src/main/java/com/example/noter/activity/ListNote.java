package com.example.noter.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.SearchManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
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

import static android.widget.Toast.LENGTH_SHORT;

public class ListNote extends AppCompatActivity {
    ImageButton add_note;
    List<TagModel> listTag;
    List<NoteModel> listNote;
    RecyclerView recyclerViewTag, recyclerViewNote;
    SharedPreferences sharedPreferences; //loading and saving notes/tags
    SharedPreferences.Editor editor;
    Gson gson;
    String current_color = String.valueOf(R.color.black);
    String currentDeleteTag;

    LinearLayout emptyLayout;

    private static final String PREF_TAG = "com.example.noter.PREFERENCES";
    private static final String TAG = "Tags";
    private static final String NOTE = "Notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);

        setUpResources();

        //set tag adapter
        if (listTag.isEmpty()) {
            recyclerViewTag.setVisibility(View.GONE);
            emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerViewTag.setVisibility(View.VISIBLE);
            emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
            emptyLayout.setVisibility(View.GONE);
            TagAdapter tagAdapter = new TagAdapter(this, R.id.list_tag, listTag, listNote);
            recyclerViewTag.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewTag.setAdapter(tagAdapter);
        }
    }

    private void setUpResources() {
        //Set up button
        add_note = (ImageButton) findViewById(R.id.add_note);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listTag.isEmpty()) {
                    Toast.makeText(ListNote.this, "Create a tag First", LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(ListNote.this, DetailNote.class);
                intent.putExtra("Mode", "New");
                intent.putExtra("ListTag", (Serializable) listTag);
                intent.putExtra("ListNote", (Serializable) listNote);
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
                Intent intent = new Intent(ListNote.this, SearchResult.class);
                intent.putExtra("ListTag", (Serializable) listTag);
                intent.putExtra("ListNote", (Serializable) listNote);
                List<NoteModel> result = search(query);
                intent.putExtra("SearchResult", (Serializable) result);
                startActivity(intent);
                return true;
            }

            private List<NoteModel> search(String query) {
                List<NoteModel> result = new ArrayList<>();

                for (NoteModel item : listNote) {
                    if (item.getNoteTitle().contains(query))
                        result.add(item);
                }

                return result;
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
                createDeleteTagDialog();
                return true;
            case R.id.btn_edit_tag:
                createEditTagDialog();
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

    public void createEditTagDialog() {
        AlertDialog.Builder editDialogBuilder;
        AlertDialog editDialog;

        editDialogBuilder = new AlertDialog.Builder(this);
        final View editTagView = getLayoutInflater().inflate(R.layout.edit_tag_dialog, null);

        editDialogBuilder.setView(editTagView);
        editDialog = editDialogBuilder.create();
        editDialog.show();

        Spinner note_tag = (Spinner) editTagView.findViewById(R.id.note_tag);
        EditText tag_name = (EditText) editTagView.findViewById(R.id.tag_name);
        Button pick_color = (Button) editTagView.findViewById(R.id.btn_tag_color);
        Button save = (Button) editTagView.findViewById(R.id.btn_edit);

        //set up spinner
        setUpSpinner(note_tag);

        //set up picking new color
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
                            Toast.makeText(ListNote.this, "Tag name existed! Choose another one.", LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                //find tag
                int pos_to_edit;
                for (pos_to_edit=0; pos_to_edit<listTag.size(); pos_to_edit++) {
                    if (listTag.get(pos_to_edit).getName().equals(currentDeleteTag))
                        break;
                }

                //edit tag
                listTag.get(pos_to_edit).setTagModel(tag_name_string, current_color);

                //edit all note tag
                for (int i=0; i<listNote.size(); i++) {
                    if (listNote.get(i).getNoteTag().equals(currentDeleteTag))
                        listNote.get(i).editNoteTag(tag_name_string);
                }

                //save tag
                saveTag(TAG, gson.toJson(listTag));
                saveTag(NOTE, gson.toJson(listNote));

                Toast.makeText(ListNote.this, "Tag edited", LENGTH_SHORT).show();
                editDialog.cancel();

                //refresh activity
                finish();
                startActivity(getIntent());
            }
        });
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
                            Toast.makeText(ListNote.this, "Tag name existed! Choose another one.", LENGTH_SHORT).show();
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
                Toast.makeText(ListNote.this, "Tag added", LENGTH_SHORT).show();
                addDialog.cancel();

                //refresh activity
                finish();
                startActivity(getIntent());
            }
        });
    }

    public void createDeleteTagDialog() {
        AlertDialog.Builder deleteDialogBuilder;
        AlertDialog deleteDialog;

        deleteDialogBuilder = new AlertDialog.Builder(this);
        final View deleteTagView = getLayoutInflater().inflate(R.layout.delete_tag_dialog, null);

        deleteDialogBuilder.setView(deleteTagView);
        deleteDialog = deleteDialogBuilder.create();
        deleteDialog.show();

        Spinner note_tag = (Spinner) deleteTagView.findViewById(R.id.note_tag);
        Button delete = (Button) deleteTagView.findViewById(R.id.btn_delete);
        setUpSpinner(note_tag);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (NoteModel item : listNote) {
                    if (item.getNoteTag().equals(currentDeleteTag)) {
                        Toast.makeText(ListNote.this, "Tag contain notes, delete notes first!", LENGTH_SHORT).show();
                        return;
                    }
                }

                //find tag
                int pos_to_del;
                for (pos_to_del=0; pos_to_del<listTag.size(); pos_to_del++) {
                    if (listTag.get(pos_to_del).getName().equals(currentDeleteTag))
                        break;
                }
                //delete tag
                listTag.remove(pos_to_del);

                //save tag
                saveTag(TAG, gson.toJson(listTag));
                Toast.makeText(ListNote.this, "Tag deleted", LENGTH_SHORT).show();
                deleteDialog.cancel();

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

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    private Spinner setUpSpinner(Spinner note_tag) {
        List<String> list_tag = new ArrayList<>();

        for (TagModel item : listTag) {
            list_tag.add(item.getName());
        }

        currentDeleteTag = list_tag.get(0);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.spinner_item, list_tag);
        note_tag.setAdapter(adp);

        note_tag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentDeleteTag = list_tag.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        return note_tag;
    }
}