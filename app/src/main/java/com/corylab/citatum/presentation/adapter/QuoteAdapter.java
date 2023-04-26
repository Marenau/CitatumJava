package com.corylab.citatum.presentation.adapter;

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

public class QuoteAdapter extends ListAdapter<Quote, QuoteAdapter.QuoteViewHolder> {

    private static final DiffUtil.ItemCallback<Quote> DIFF_CALLBACK = new DiffUtil.ItemCallback<Quote>() {
        @Override
        public boolean areItemsTheSame(@NonNull Quote oldItem, @NonNull Quote newItem) {
            return oldItem.getUid() == newItem.getUid();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Quote oldItem, @NonNull Quote newItem) {
            return oldItem.equals(newItem);
        }
    };

    public QuoteAdapter() {
        super(DIFF_CALLBACK);
    }

    public final static class QuoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, authorText, dataText;

        public QuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.qi_title_text);
            authorText = itemView.findViewById(R.id.qi_author_text);
            dataText = itemView.findViewById(R.id.qi_data_text);
        }
    }

    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View quotesItems = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_item, parent, false);
        return new QuoteViewHolder(quotesItems);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        Quote quote = getItem(position);
        holder.titleText.setText(quote.getTitle());
        holder.authorText.setText(quote.getAuthor());
        holder.dataText.setText(quote.getDate());
        holder.itemView.setOnClickListener(view -> {
            Bundle transfer = new Bundle();
            transfer.putInt("uid", quote.getUid());
            Navigation.findNavController(view).navigate(R.id.quoteCreateFragment, transfer);
        });
    }

    public Quote getQuoteAtPosition(int position) {
        return getItem(position);
    }
}