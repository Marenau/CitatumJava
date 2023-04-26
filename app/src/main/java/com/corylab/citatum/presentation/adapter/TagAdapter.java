package com.corylab.citatum.presentation.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.corylab.citatum.R;
import com.corylab.citatum.data.model.Quote;
import com.corylab.citatum.data.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends ListAdapter<Tag, TagAdapter.TagViewHolder> {

    private static final DiffUtil.ItemCallback<Tag> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tag>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tag oldItem, @NonNull Tag newItem) {
            return oldItem.getUid() == newItem.getUid();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tag oldItem, @NonNull Tag newItem) {
            return oldItem.equals(newItem);
        }
    };

    public TagAdapter() {
        super(DIFF_CALLBACK);
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
        View tagsItems = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        return new TagViewHolder(tagsItems);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        Tag tag = getItem(position);
        holder.tagName.setText(tag.getName());
        holder.itemView.setOnClickListener(view -> {
            Bundle transfer = new Bundle();
            transfer.putInt( "uid", tag.getUid());
            Navigation.findNavController(view).navigate(R.id.action_tagsFragment_to_tagFragment, transfer);
        });
    }

    public Tag getTagAtPosition(int position) {
        return getItem(position);
    }
}
