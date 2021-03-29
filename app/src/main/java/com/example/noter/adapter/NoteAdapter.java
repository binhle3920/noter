package com.example.noter.adapter;

import android.content.Context;
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
import com.example.noter.model.NoteModel;
import com.example.noter.model.TagModel;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter {
    List<NoteModel> items;
    TagModel tag;
    Context context;

    public NoteAdapter(@NonNull Context context, int resource, List<NoteModel> items, TagModel tag) {
        this.items = items;
        this.tag = tag;
        this.context = context;
        Log.i("NOTES", String.valueOf(items));
    }

    //item holder class
    public class NoteItemHolder extends RecyclerView.ViewHolder {
        private Button btn;
        private TextView date;

        private NoteItemHolder(View view) {
            super(view);
            btn = (Button) view.findViewById(R.id.btn_note);
            date = (TextView) view.findViewById(R.id.note_date);
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
