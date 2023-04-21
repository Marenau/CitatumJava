package com.corylab.citatum.presentation.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.corylab.citatum.R;
import com.corylab.citatum.presentation.activity.MainActivity;
import com.corylab.citatum.databinding.FragmentLeftNavigationMenuBinding;

public class LeftNavigationFragment extends Fragment {

    private FragmentLeftNavigationMenuBinding binding;
    private MainActivity activity;

    public LeftNavigationFragment() {
        super(R.layout.fragment_left_navigation_menu);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        activity = (MainActivity) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLeftNavigationMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.image_scale);

        binding.lmSettingsTv.setOnClickListener(view -> {
            binding.lmSettingsTv.startAnimation(animation);
            navigate(R.id.settingsFragment);
        });
        binding.lmPencilTv.setOnClickListener(view -> {
            binding.lmPencilTv.startAnimation(animation);
            navigate(R.id.quoteCreateFragment);
        });
        binding.lmHubTv.setOnClickListener(view1 -> {
            binding.lmHubTv.startAnimation(animation);
            navigate(R.id.hubFragment);
        });
        binding.lmRepositoryTv.setOnClickListener(view2 -> {
            binding.lmRepositoryTv.startAnimation(animation);
            navigate(R.id.repositoryFragment);
        });
        binding.lmTagsTv.setOnClickListener(view -> {
            binding.lmTagsTv.startAnimation(animation);
            navigate(R.id.tagsFragment);
        });
        binding.lmBookmarkTv.setOnClickListener(view -> {
            binding.lmBookmarkTv.startAnimation(animation);
            navigate(R.id.bookmarksFragment);
        });
        binding.lmBasketTv.setOnClickListener(view -> {
            binding.lmBasketTv.startAnimation(animation);
            navigate(R.id.basketFragment);
        });
        binding.lmFeedbackTv.setOnClickListener(view -> {
            binding.lmFeedbackTv.startAnimation(animation);
            DrawerLayout drawer = activity.findViewById(R.id.ma_drawer);
            drawer.closeDrawer(GravityCompat.START);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_address));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text));
            if (emailIntent.resolveActivity(activity.getPackageManager()) != null) {
                startActivity(Intent.createChooser(emailIntent, "Open the application to send email"));
            }
        });
    }

    private void navigate(int id) {
        View mainContainer = activity.findViewById(R.id.nav_container_view);
        NavController navController = Navigation.findNavController(mainContainer);
        int currentDestination = navController.getCurrentDestination().getId();
        if (currentDestination != id) {
            navController.navigate(id);
        }
        DrawerLayout drawer = activity.findViewById(R.id.ma_drawer);
        drawer.closeDrawer(GravityCompat.START);
    }
}