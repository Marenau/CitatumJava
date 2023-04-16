package com.corylab.citatum.presentation.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.corylab.citatum.R;
import com.corylab.citatum.enumeration.AccountStatus;
import com.corylab.citatum.presentation.activity.LoginActivity;
import com.corylab.citatum.databinding.FragmentRegisterBinding;
import com.corylab.citatum.presentation.fragment.dialog.ShowNotificationFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private LoginActivity activity;
    private FirebaseAuth auth;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        activity = (LoginActivity) context;
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
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        binding.rgfLoginCv.setOnClickListener(view -> Navigation.findNavController(view).navigateUp());
        binding.rgfEntranceIcon.setOnClickListener(view -> createAccount());
    }

    private void createAccount() {
        if (!isNetworkAvailable()) {
            showNotification(AccountStatus.UNCONNECTED);
            return;
        }
        String username = binding.rgfUsernameEt.getText().toString();
        String password = binding.rgfPasswordEt.getText().toString();
        String email = binding.rgfEmailEt.getText().toString();
        if (isValidNickname(username) && isPasswordSecure(password) && isValidEmail(email)) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            showNotification(AccountStatus.CREATED);
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            usersRef.child(userId).child("username").setValue(username);
                            FirebaseAuth.getInstance().signOut();
                        } else {
                            showNotification(AccountStatus.REJECTED);
                        }
                    });
        } else {
            showNotification(AccountStatus.INCORRECT);
        }
    }

    private void showNotification(AccountStatus status) {
        String title = "", message = "", button = "";
        if (status == AccountStatus.UNCONNECTED) {
            title = getString(R.string.no_connection_title_text);
            message = getString(R.string.no_connection_message_text);
            button = getString(R.string.no_connection_button_text);
        } else if (status == AccountStatus.CREATED) {
            title = getString(R.string.success_register_title_text);
            message = getString(R.string.success_register_message_text);
            button = getString(R.string.success_register_button_text);
        } else if (status == AccountStatus.REJECTED) {
            title = getString(R.string.rejected_register_title_text);
            message = getString(R.string.rejected_register_message_text);
            button = getString(R.string.rejected_register_button_text);
        } else if (status == AccountStatus.INCORRECT) {
            title = getString(R.string.incorrect_register_title_text);
            message = getString(R.string.incorrect_register_message_text);
            button = getString(R.string.incorrect_register_button_text);
        }
        ShowNotificationFragment dialog = ShowNotificationFragment.newInstance(title, message, button);
        dialog.show(getParentFragmentManager(), "dialog");
    }

    private boolean isPasswordSecure(String password) {
        String regex = "^(?=.*\\d).{8,}$";
        return password.matches(regex);
    }

    private boolean isValidNickname(String nickname) {
        String regex = "^[a-zA-Z0-9_-]{1,20}$";
        return nickname.matches(regex);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
