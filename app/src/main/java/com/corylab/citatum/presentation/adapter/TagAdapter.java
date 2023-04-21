package com.corylab.citatum.presentation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corylab.citatum.R;
import com.corylab.citatum.data.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private Context context;
    private List<Tag> tags;

    public TagAdapter(Context context) {
        this.context = context;
        tags = new ArrayList<>();
    }

    public final static class TagViewHolder extends RecyclerView.ViewHolder {

        TextView tagName;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.ti_tag_name);
        }
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tagsItems = LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false);
        return new TagViewHolder(tagsItems);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        Tag tag = tags.get(position);
        holder.tagName.setText(tag.getName());
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Tag> tags) {
        this.tags.clear();
        this.tags = tags;
        notifyDataSetChanged();
    }
}
