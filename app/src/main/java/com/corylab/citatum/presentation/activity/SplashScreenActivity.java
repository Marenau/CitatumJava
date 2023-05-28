package com.corylab.citatum.presentation.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.corylab.citatum.R;
import com.corylab.citatum.network.ForismaticQuote;
import com.corylab.citatum.presentation.viewmodel.NetworkViewModel;
import com.corylab.citatum.presentation.viewmodel.QuoteViewModel;
import com.corylab.citatum.presentation.viewmodel.SharedPreferencesViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        CardView logoImageView = findViewById(R.id.ss_cv);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImageView.startAnimation(animation);

        SharedPreferencesViewModel sharedPreferencesViewModel = new ViewModelProvider(this).get(SharedPreferencesViewModel.class);
        QuoteViewModel quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
        NetworkViewModel networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);
        long lastRequestTimestamp = sharedPreferencesViewModel.getLong("last_request_timestamp",
                (Calendar.getInstance().getTimeInMillis() - 24 * 60 * 60 * 1000) - 1);
        Calendar calendar = Calendar.getInstance();
        long currentTimestamp = calendar.getTimeInMillis();

        new Handler().postDelayed(() -> {
            if (currentTimestamp - lastRequestTimestamp > 24 * 60 * 60 * 1000) {
                ForismaticQuote quote = networkViewModel.getQuote();
                if (quote != null) {
                    sharedPreferencesViewModel.setString("quote_text", quote.getQuoteText());
                    sharedPreferencesViewModel.setString("quote_author", quote.getQuoteAuthor());
                    sharedPreferencesViewModel.setLong("last_request_timestamp", currentTimestamp);
                }
            }

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent;
            if (currentUser != null) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            this.startActivity(intent);
            this.finish();

            quoteViewModel.removeOutdatedQuotes();
        }, 1000);
    }
}