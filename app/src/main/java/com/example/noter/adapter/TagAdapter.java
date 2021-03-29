package com.example.noter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noter.R;
import com.example.noter.model.TagModel;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter {
    List<TagModel> items;
    Context context;

    //constructor
    public TagAdapter(@NonNull Context context, int resource, @NonNull List<TagModel> items) {
        this.context = context;
        this.items = items;
    }

    //item holder class
    public class TagItemHolder extends RecyclerView.ViewHolder {
        private Button btn;
        private TagItemHolder(View view) {
            super(view);
            btn = (Button) view.findViewById(R.id.button);
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
}
