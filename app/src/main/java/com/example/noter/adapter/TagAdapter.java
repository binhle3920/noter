package com.example.noter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noter.R;
import com.example.noter.model.NoteModel;
import com.example.noter.model.TagModel;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter {
    List<TagModel> items;
    List<NoteModel> listNote;
    Context context;
    RecyclerView recyclerViewNote;
    LinearLayout emptyLayout;

    //constructor
    public TagAdapter(@NonNull Context context, int resource, @NonNull List<TagModel> items, List<NoteModel> listNote) {
        this.context = context;
        this.listNote = listNote;
        this.items = items;

        //create note adapter with the first tag
        recyclerViewNote = (RecyclerView) ((Activity) context).findViewById(R.id.list_note);
        emptyLayout = (LinearLayout) ((Activity) context).findViewById(R.id.empty_layout);
        List<NoteModel> listNoteOfTag = findListNotes(items.get(0));
        //check if empty
        if (listNoteOfTag.size() == 0) {
            recyclerViewNote.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerViewNote.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            NoteAdapter noteAdapter = new NoteAdapter(context, R.id.list_note, listNoteOfTag, items.get(0));
            recyclerViewNote.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerViewNote.setAdapter(noteAdapter);
        }
    }

    //item holder class
    public class TagItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Button btn;
        private TagItemHolder(View view) {
            super(view);
            btn = (Button) view.findViewById(R.id.button);
            view.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View view) {
            List<NoteModel> listNoteOfTag = findListNotes(items.get(getLayoutPosition()));
            if (listNoteOfTag.size() == 0) {
                recyclerViewNote.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            } else {
                recyclerViewNote.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
                NoteAdapter noteAdapter = new NoteAdapter(context, R.id.list_note, listNoteOfTag, items.get(getLayoutPosition()));
                recyclerViewNote.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                recyclerViewNote.setAdapter(noteAdapter);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag_item, parent, false);
        return new TagItemHolder(item);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TagModel item = items.get(position);
        TagItemHolder tagItemHolder = (TagItemHolder) holder;

        tagItemHolder.btn.setBackgroundColor(item.getColor());
        tagItemHolder.btn.setText(item.getName());
    }

    public int getItemCount() { return items.size(); }

    private List<NoteModel> findListNotes(TagModel tag) {
        List<NoteModel> listNoteTag = new ArrayList<>();

        for (NoteModel item : listNote) {
            if (item.getNoteTag().equals(tag.getName())) {
                listNoteTag.add(item);
            }
        }

        return listNoteTag;
    }
}
