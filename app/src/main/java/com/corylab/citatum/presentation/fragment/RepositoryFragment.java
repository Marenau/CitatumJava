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
import com.corylab.citatum.databinding.FragmentRepositoryBinding;
import com.corylab.citatum.presentation.viewmodel.QuoteViewModel;
import com.corylab.citatum.presentation.adapter.QuoteAdapter;

public class RepositoryFragment extends Fragment {

    private FragmentRepositoryBinding binding;
    private QuoteViewModel quoteViewModel;

    public RepositoryFragment() {
        super(R.layout.fragment_repository);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRepositoryBinding.inflate(inflater, container, false);
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
        binding.rfRecyclerView.setLayoutManager(layoutManager);
        QuoteAdapter quoteAdapter = new QuoteAdapter(getContext());
        binding.rfRecyclerView.setAdapter(quoteAdapter);
        quoteViewModel.getQuotes().observe(getViewLifecycleOwner(), quotes -> quoteAdapter.updateList(quotes));
    }
}
