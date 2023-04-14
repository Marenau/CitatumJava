package com.corylab.citatum.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.corylab.citatum.R;
import com.corylab.citatum.activity.MainActivity;
import com.corylab.citatum.databinding.FragmentLeftNavigationMenuBinding;

import java.util.Locale;

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

        binding.lmPencilTv.setOnClickListener(view -> {
            navigate(R.id.quoteCreateFragment);
        });
        binding.lmHubTv.setOnClickListener(view1 -> {
            navigate(R.id.hubFragment);
        });
        binding.lmRepositoryTv.setOnClickListener(view2 -> {
            navigate(R.id.repositoryFragment);
        });
        binding.lmTagsTv.setOnClickListener(view -> {
            navigate(R.id.tagsFragment);
        });
        binding.lmBookmarkTv.setOnClickListener(view -> {
            navigate(R.id.bookmarksFragment);
        });
        binding.lmBasketTv.setOnClickListener(view -> {
            navigate(R.id.basketFragment);
        });
        binding.lmFeedbackTv.setOnClickListener(view -> {
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
        DrawerLayout drawer = activity.findViewById(R.id.ma_drawer);
        NavController navController = Navigation.findNavController(mainContainer);
        int currentDestination = navController.getCurrentDestination().getId();
        if (currentDestination != id) {
            navController.navigate(id);
        }
        drawer.closeDrawer(GravityCompat.START);
    }
}