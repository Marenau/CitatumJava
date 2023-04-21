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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corylab.citatum.R;
import com.corylab.citatum.databinding.FragmentTagsBinding;
import com.corylab.citatum.presentation.activity.MainActivity;
import com.corylab.citatum.presentation.adapter.TagAdapter;
import com.corylab.citatum.presentation.viewmodel.TagViewModel;

public class TagsFragment extends Fragment {

    private FragmentTagsBinding binding;
    private MainActivity activity;
    private TagViewModel tagViewModel;

    public TagsFragment() {
        super(R.layout.fragment_tags);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        activity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTagsBinding.inflate(inflater, container, false);
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
        binding.tgfRecyclerView.setLayoutManager(layoutManager);
        TagAdapter tagAdapter = new TagAdapter(getContext());
        binding.tgfRecyclerView.setAdapter(tagAdapter);
        tagViewModel.getTags().observe(getViewLifecycleOwner(), tags -> tagAdapter.updateList(tags));

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.image_scale);
        binding.tgfTagIcon.setOnClickListener(view -> {
            binding.tgfTagIcon.startAnimation(animation);
            Navigation.findNavController(view).navigate(R.id.action_tagsFragment_to_tagCreateFragment);
        });
    }
}
