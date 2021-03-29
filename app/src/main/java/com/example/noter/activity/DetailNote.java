package com.example.noter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noter.R;
import com.example.noter.model.NoteModel;
import com.example.noter.model.TagModel;
import com.google.gson.Gson;

import net.dankito.richtexteditor.android.RichTextEditor;
import net.dankito.richtexteditor.android.toolbar.AllCommandsEditorToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailNote extends AppCompatActivity {
    TextView note_day;
    EditText note_title;
    String tag;
    NoteModel note;
    Boolean edit = false;
    NoteModel currentNote;

    //Input note framework
    private RichTextEditor note_content;
    private AllCommandsEditorToolbar editorToolbar;

    //loading and saving notes/tags
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    private static final String PREF_TAG = "com.example.noter.PREFERENCES";
    private static final String NOTE = "Notes";

    Spinner note_tag;
    List<TagModel> listTag;
    List<NoteModel> listNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get intent

        listTag = (List<TagModel>) getIntent().getSerializableExtra("ListTag");
        listNote = (List<NoteModel>) getIntent().getSerializableExtra("ListNote");
        if (listNote == null) {
            listNote = new ArrayList<>();
        }

        if (getIntent().getStringExtra("Mode").equals("Edit")) {
            currentNote = (NoteModel) getIntent().getSerializableExtra("Note");
            edit = true;
        }

        setUpSpinner();
        setUpRecourse();

        //set current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        note_day.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void setUpRecourse() {
        note_day = findViewById(R.id.note_date);

        //editor setup
        note_title = findViewById(R.id.note_title);
        note_day = findViewById(R.id.note_date);

        note_content = (RichTextEditor) findViewById(R.id.note_content);
        editorToolbar = (AllCommandsEditorToolbar) findViewById(R.id.editorToolbar);
        editorToolbar.setEditor(note_content);
        note_content.setEditorFontSize(20);
        note_content.setPadding((int) (4 * getResources().getDisplayMetrics().density));
        // some properties you also can set on editor
        // editor.setEditorBackgroundColor(Color.YELLOW);
        // editor.setEditorFontColor(Color.MAGENTA);
        // editor.setEditorFontFamily("cursive");
        // show keyboard right at start up
        // editor.focusEditorAndShowKeyboardDelayed();

        //SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();

        //set up text if this is edit
        if (edit == true) {
            note_day.setText(currentNote.getNoteDate());
            note_title.setText(currentNote.getNoteTitle());
            note_content.setHtml(currentNote.getNoteContent());
            for (int i = 0; i < listTag.size(); i++) {
                if (listTag.get(i).getName().equals(currentNote.getNoteTag())) {
                    note_tag.setSelection(i);
                }
            }
            note_tag.setEnabled(false);
        }

    };

    @Override
    public void onBackPressed() {
        if(editorToolbar.handlesBackButtonPress() == false) {
            super.onBackPressed();
        }
    }

    private void setUpSpinner() {
        List<String> list_tag = new ArrayList<>();

        for (TagModel item : listTag) {
            list_tag.add(item.getName());
        }

        tag = list_tag.get(0);
        note_tag = (Spinner) findViewById(R.id.note_tag);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.spinner_item, list_tag);
        note_tag.setAdapter(adp);

        note_tag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tag = list_tag.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //create save button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.save:
                note = new NoteModel();

                //handle error
                if (TextUtils.isEmpty(note_title.getText())) {
                    note_title.setError("Title is required");
                    return false;
                }

                note.setNoteModel(String.valueOf(note_title.getText()), note_content.getHtml(), String.valueOf(note_day.getText()), tag);
                listNote.add(note);
                saveNote(NOTE, gson.toJson(listNote));
                Toast.makeText(DetailNote.this, "Note saved", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(DetailNote.this, ListNote.class);
                startActivity(intent);
        }

        return false;
    }

    private void saveNote(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
}