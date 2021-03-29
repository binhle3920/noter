package com.example.noter.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noter.R;
import com.example.noter.activity.DetailNote;
import com.example.noter.activity.ListNote;
import com.example.noter.model.NoteModel;
import com.example.noter.model.TagModel;

import java.io.Serializable;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter {
    List<NoteModel> items, allItems;
    List<TagModel> allTags;
    TagModel tag;
    Context context;

    public NoteAdapter(@NonNull Context context, int resource, List<NoteModel> items, TagModel tag, List<NoteModel> allItems, List<TagModel> allTags) {
        this.items = items;
        this.tag = tag;
        this.context = context;
        //we need those variable for opening adding activiy
        this.allItems = allItems;
        this.allTags = allTags;
    }

    //item holder class
    public class NoteItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private Button btn;
        private TextView date;

        private NoteItemHolder(View view) {
            super(view);
            btn = (Button) view.findViewById(R.id.btn_note);
            date = (TextView) view.findViewById(R.id.note_date);
            btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailNote.class);
            intent.putExtra("Mode", "Edit");
            intent.putExtra("ListTag", (Serializable) allTags);
            intent.putExtra("ListNote", (Serializable) allItems);

            for (NoteModel note : items) {
                if (note.getNoteTitle().equals(String.valueOf(btn.getText()))) {
                    intent.putExtra("Note", (Serializable) note);
                }
            }
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_item, parent, false);
        return new NoteItemHolder(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NoteModel item = items.get(position);
        NoteItemHolder noteItemHolder = (NoteItemHolder) holder;

        noteItemHolder.btn.setBackgroundColor(tag.getColor());
        noteItemHolder.btn.setText(item.getNoteTitle());
        noteItemHolder.date.setText(item.getNoteDate());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
