package com.example.noter.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noter.R;
import com.example.noter.activity.DetailNote;
import com.example.noter.model.NoteModel;
import com.example.noter.model.TagModel;

import java.io.Serializable;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter {
    List<NoteModel> listNote, listResult;
    List<TagModel> listTag;
    Context context;

    public ResultAdapter(@NonNull Context context, int resource, List<NoteModel> listResult, List<NoteModel> listNote, List<TagModel> listTag) {
        this.listNote = listNote;
        this.listTag = listTag;
        this.listResult = listResult;
        this.context = context;
    }

    //item holder class
    public class NoteItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            intent.putExtra("ListTag", (Serializable) listTag);
            intent.putExtra("ListNote", (Serializable) listNote);

            for (NoteModel note : listResult) {
                if (note.getNoteTitle().equals(String.valueOf(btn.getText()))) {
                    intent.putExtra("Note", (Serializable) note);
                }
            }
            context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_item, parent, false);
        return new NoteItemHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NoteModel note = listResult.get(position);
        NoteItemHolder noteItemHolder = (NoteItemHolder) holder;

        //find tag of this note
        int color = listTag.get(0).getColor();
        for (TagModel item : listTag) {
            if (item.getName().equals(note.getNoteTag())) {
                color = item.getColor();
                break;
            }
        }

        //set background and text
        noteItemHolder.btn.setBackgroundColor(color);
        noteItemHolder.btn.setText(note.getNoteTitle());
        noteItemHolder.date.setText(note.getNoteDate());
    }

    @Override
    public int getItemCount() {
        return listResult.size();
    }
}
