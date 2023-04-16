package com.corylab.citatum.presentation.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.corylab.citatum.R;
import com.corylab.citatum.databinding.FragmentSettingsBinding;
import com.corylab.citatum.presentation.activity.LoginActivity;
import com.corylab.citatum.presentation.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private MainActivity activity;
    private FirebaseAuth auth;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        activity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        FirebaseUser user = auth.getCurrentUser();
        long creationDate = user.getMetadata().getCreationTimestamp();
        String formattedDate = new SimpleDateFormat("MM/yyyy").format(new Date(creationDate));
        binding.sfDateText.setText(getString(R.string.sf_date_text) + " " + formattedDate);
        binding.sfEmailText.setText(user.getEmail());

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        String userId = user.getUid();
        usersRef.child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                binding.sfUsernameText.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase Error", "onCancelled: " + databaseError.getMessage());
            }
        });

        binding.sfQuitBtn.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
        });
    }
}
