package com.corylab.citatum.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.corylab.citatum.R;
import com.corylab.citatum.data.model.Tag;
import com.corylab.citatum.databinding.FragmentTagCreateBinding;
import com.corylab.citatum.presentation.activity.MainActivity;
import com.corylab.citatum.presentation.viewmodel.TagViewModel;

public class TagCreateFragment extends Fragment {

    private FragmentTagCreateBinding binding;
    private MainActivity activity;
    private TagViewModel tagViewModel;

    public TagCreateFragment() {
        super(R.layout.fragment_tag_create);
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
        binding = FragmentTagCreateBinding.inflate(inflater, container, false);
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
        binding.tcfNameEt.requestFocus();
        InputMethodManager inputMethodManagerShow = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManagerShow.showSoftInput(binding.tcfNameEt, InputMethodManager.SHOW_IMPLICIT);

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.image_scale);

        binding.tcfConfirmIcon.setOnClickListener(view -> {
            binding.tcfConfirmIcon.startAnimation(animation);
            tagViewModel.insert(new Tag(binding.tcfNameEt.getText().toString()));
            Navigation.findNavController(view).navigateUp();
        });
    }
}
