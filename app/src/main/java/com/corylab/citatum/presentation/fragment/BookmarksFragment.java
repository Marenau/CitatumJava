package com.corylab.citatum.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corylab.citatum.R;
import com.corylab.citatum.databinding.FragmentBookmarksBinding;
import com.corylab.citatum.presentation.adapter.QuoteAdapter;
import com.corylab.citatum.presentation.viewmodel.QuoteViewModel;

public class BookmarksFragment extends Fragment {

    private FragmentBookmarksBinding binding;
    private QuoteViewModel quoteViewModel;

    public BookmarksFragment() {
        super(R.layout.fragment_bookmarks);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookmarksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        } else {
            return AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        }
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.bmfRecyclerView.setLayoutManager(layoutManager);
        QuoteAdapter quoteAdapter = new QuoteAdapter(getContext());
        binding.bmfRecyclerView.setAdapter(quoteAdapter);
        quoteViewModel.getQuotes().observe(getViewLifecycleOwner(), quotes -> quoteAdapter.updateList(quotes, 1));
    }
}
