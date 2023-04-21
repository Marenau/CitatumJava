package com.corylab.citatum.presentation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.corylab.citatum.R;
import com.corylab.citatum.data.model.Quote;

import java.util.ArrayList;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder> {

    private Context context;
    private List<Quote> quotes;

    public QuoteAdapter(Context context) {
        this.context = context;
        quotes = new ArrayList<>();
    }

    public final static class QuoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, authorText, dataText;
        RecyclerView tagsList;

        public QuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.qi_title_text);
            authorText = itemView.findViewById(R.id.qi_author_text);
            dataText = itemView.findViewById(R.id.qi_data_text);
            tagsList = itemView.findViewById(R.id.qi_tags_rv);
        }
    }

    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View quotesItems = LayoutInflater.from(context).inflate(R.layout.quote_item, parent, false);
        return new QuoteViewHolder(quotesItems);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        Quote quote = quotes.get(position);
        holder.titleText.setText(quote.getTitle());
        holder.authorText.setText(quote.getAuthor());
        holder.dataText.setText(quote.getDate());
        holder.itemView.setOnClickListener(view -> {
            Bundle transfer = new Bundle();
            transfer.putInt("uid", quote.getUid());
            Navigation.findNavController(view).navigate(R.id.quoteCreateFragment, transfer);
        });
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Quote> quotes) {
        this.quotes.clear();
        this.quotes = quotes;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Quote> quotes, int param) {
        this.quotes.clear();
        List<Quote> temp = new ArrayList<>();
        for (Quote q : quotes) {
            if (q.getBookmarkFlag() == param)
                temp.add(q);
        }
        this.quotes = temp;
        notifyDataSetChanged();
    }
}
