package com.corylab.citatum.presentation.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.corylab.citatum.R;
import com.corylab.citatum.data.model.Quote;
import com.corylab.citatum.presentation.viewmodel.QuoteTagJoinViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class QuoteAdapter extends ListAdapter<Quote, QuoteAdapter.QuoteViewHolder> {

    private Fragment fragment;
    private QuoteTagJoinViewModel quoteTagJoinViewModel;

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

    public QuoteAdapter(Fragment fragment) {
        super(DIFF_CALLBACK);
        this.fragment = fragment;
        this.quoteTagJoinViewModel = new ViewModelProvider(fragment).get(QuoteTagJoinViewModel.class);
    }

    public final static class QuoteViewHolder extends RecyclerView.ViewHolder {

        TextView quoteText, authorTitleText, dataText;
        RecyclerView tagsRv;

        public QuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            quoteText = itemView.findViewById(R.id.qi_quote_text);
            authorTitleText = itemView.findViewById(R.id.qi_author_title_text);
            dataText = itemView.findViewById(R.id.qi_data_text);
            tagsRv = itemView.findViewById(R.id.qi_rv);
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
        holder.quoteText.setText(quote.getText());
        holder.authorTitleText.setText("© " + quote.getAuthor() + ", " + quote.getTitle());
        holder.dataText.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(quote.getDate()));
        holder.itemView.setOnClickListener(view -> {
            Bundle transfer = new Bundle();
            transfer.putInt("uid", quote.getUid());
            Navigation.findNavController(view).navigate(R.id.quoteCreateFragment, transfer);
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(fragment.getContext(), RecyclerView.HORIZONTAL, false);
        holder.tagsRv.setLayoutManager(layoutManager);
        QuoteTagAdapter adapter = new QuoteTagAdapter();
        holder.tagsRv.setAdapter(adapter);
        quoteTagJoinViewModel.getTagsForQuote(quote.getUid()).observe(fragment.getViewLifecycleOwner(), tags -> {
            adapter.submitList(tags);
        });
    }

    public Quote getQuoteAtPosition(int position) {
        return getItem(position);
    }
}