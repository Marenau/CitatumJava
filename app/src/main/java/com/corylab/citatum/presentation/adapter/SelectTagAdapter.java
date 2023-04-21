package com.corylab.citatum.presentation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corylab.citatum.R;
import com.corylab.citatum.data.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class SelectTagAdapter extends RecyclerView.Adapter<SelectTagAdapter.SelectTagViewHolder> {

    private Context context;
    private List<Tag> tags;

    public SelectTagAdapter(Context context) {
        this.context = context;
        tags = new ArrayList<>();
    }

    public final static class SelectTagViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public SelectTagViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.sti_chb);
        }
    }

    @NonNull
    @Override
    public SelectTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tagsItems = LayoutInflater.from(context).inflate(R.layout.select_tag_item, parent, false);
        return new SelectTagViewHolder(tagsItems);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTagViewHolder holder, int position) {
        Tag tag = tags.get(position);
        holder.checkBox.setText(tag.getName());
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
