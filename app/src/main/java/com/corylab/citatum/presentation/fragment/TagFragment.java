package com.corylab.citatum.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corylab.citatum.R;
import com.corylab.citatum.data.model.Quote;
import com.corylab.citatum.data.model.Tag;
import com.corylab.citatum.databinding.FragmentTagBinding;
import com.corylab.citatum.presentation.activity.MainActivity;
import com.corylab.citatum.presentation.adapter.QuoteAdapter;
import com.corylab.citatum.presentation.viewmodel.QuoteTagJoinViewModel;
import com.corylab.citatum.presentation.viewmodel.QuoteViewModel;
import com.corylab.citatum.presentation.viewmodel.TagViewModel;

import java.util.List;

public class TagFragment extends Fragment {

    private FragmentTagBinding binding;
    private MainActivity activity;
    private QuoteTagJoinViewModel quoteTagJoinViewModel;
    private TagViewModel tagViewModel;

    public TagFragment() {
        super(R.layout.fragment_tag);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        activity = (MainActivity) context;
        super.onAttach(context);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);
        quoteTagJoinViewModel = new ViewModelProvider(this).get(QuoteTagJoinViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTagBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        Bundle trBundle = getArguments();
        int uid = trBundle.getInt("uid");
        binding.tfTitleText.setText(tagViewModel.getTagById(uid).getName());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.tfRv.setLayoutManager(layoutManager);
        QuoteAdapter quoteAdapter = new QuoteAdapter();
        binding.tfRv.setAdapter(quoteAdapter);
        quoteTagJoinViewModel.getQuotesForTag(uid).observe(getViewLifecycleOwner(), quotes -> quoteAdapter.submitList(quotes));
    }
}
