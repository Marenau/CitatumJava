package com.corylab.citatum.presentation.fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.corylab.citatum.R;
import com.corylab.citatum.data.model.Quote;
import com.corylab.citatum.databinding.FragmentQuoteCreateBinding;
import com.corylab.citatum.presentation.viewmodel.QuoteViewModel;
import com.corylab.citatum.presentation.activity.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class QuoteCreateFragment extends Fragment {

    private FragmentQuoteCreateBinding binding;
    private MainActivity activity;
    private QuoteViewModel quoteViewModel;

    public QuoteCreateFragment() {
        super(R.layout.fragment_quote_create);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        activity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuoteCreateBinding.inflate(inflater, container, false);
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
            return AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        } else {
            return AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        }
    }

    private void init() {
        final boolean[] isPressed = {false};
        final int[] uid = {-1};
        Bundle trBundle = getArguments();
        if (trBundle != null) {
            if (trBundle.containsKey("uid")) {
                uid[0] = trBundle.getInt("uid");
                Quote thisQuote = quoteViewModel.getQuoteByUid(uid[0]);
                binding.qcTitleEt.setText(thisQuote.getTitle());
                binding.qcAuthorEt.setText(thisQuote.getAuthor());
                binding.qcTextEt.setText(thisQuote.getText());
                binding.qcPageNumberEt.setText(thisQuote.getPageNumber());
                if (thisQuote.getBookmarkFlag() == 1) {
                    setBookmarkColor(R.color.light_red);
                    isPressed[0] = true;
                }
            } else {
                uid[0] = quoteViewModel.getMaxId() + 1;
            }
        } else {
            binding.qcTextEt.requestFocus();
            InputMethodManager inputMethodManagerShow = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManagerShow.showSoftInput(binding.qcTextEt, InputMethodManager.SHOW_IMPLICIT);
        }
        if (quoteViewModel.getMaxId() == 0) {
            binding.qcTagIcon.setVisibility(View.INVISIBLE);
            binding.qcBookmarkIcon.setVisibility(View.INVISIBLE);
            binding.qcBasketIcon.setVisibility(View.INVISIBLE);
        }

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.image_scale);

        binding.qcConfirmIcon.setOnClickListener(view -> {
            binding.qcConfirmIcon.startAnimation(animation);
            if (uid[0] == -1) {
                quoteViewModel.insert(genQuote());
                uid[0] = quoteViewModel.getMaxId();
                Log.i("ID", String.valueOf(uid[0]));
                createSnackbar(R.string.qc_quotes_added);
            } else {
                Quote temp = genQuote();
                temp.setUid(uid[0]);
                quoteViewModel.update(genQuote());
                createSnackbar(R.string.qc_quotes_update);
            }
            InputMethodManager inputMethodManagerHide = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManagerHide.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        binding.qcTagIcon.setOnClickListener(view -> {
            binding.qcTagIcon.startAnimation(animation);
            Navigation.findNavController(view).navigate(R.id.action_quoteCreateFragment_to_tagSelectionFragment);
        });

        binding.qcBookmarkIcon.setOnClickListener(view -> {
            binding.qcBookmarkIcon.startAnimation(animation);
            Quote temp = genQuote();
            temp.setUid(uid[0]);
            if (!isPressed[0]) {
                setBookmarkColor(R.color.light_red);
                temp.setBookmarkFlag(1);
                quoteViewModel.update(temp);
                isPressed[0] = true;
            } else {
                setBookmarkColor(R.color.black);
                temp.setBookmarkFlag(0);
                quoteViewModel.update(temp);
                isPressed[0] = false;
            }
        });

        binding.qcBasketIcon.setOnClickListener(view -> {
            binding.qcBasketIcon.startAnimation(animation);
            Quote temp = new Quote();
            temp.setUid(uid[0]);
            quoteViewModel.delete(temp);
            createSnackbar(R.string.qc_quotes_delete);
            Navigation.findNavController(view).navigateUp();
        });
    }

    private void setBookmarkColor(int id) {
        binding.qcBookmarkIcon.getDrawable().setColorFilter(
                new PorterDuffColorFilter(getResources().getColor(id, activity.getTheme()),
                        PorterDuff.Mode.SRC_IN));
    }

    private Quote genQuote() {
        binding.qcTitleEt.setText(binding.qcTitleEt.getText().toString().isEmpty() ?
                getString(R.string.qc_autosubstitution_title_text) : binding.qcTitleEt.getText().toString());
        binding.qcAuthorEt.setText(binding.qcAuthorEt.getText().toString().isEmpty() ?
                getString(R.string.qc_autosubstitution_author_text) : binding.qcAuthorEt.getText().toString());
        binding.qcTextEt.setText(binding.qcTextEt.getText().toString().isEmpty() ?
                getString(R.string.qc_autosubstitution_text_text) : binding.qcTextEt.getText().toString());
        binding.qcPageNumberEt.setText(binding.qcPageNumberEt.getText().toString().isEmpty() ?
                getString(R.string.qc_autosubstitution_page_number_text) : binding.qcPageNumberEt.getText().toString());
        String title = binding.qcTitleEt.getText().toString().trim();
        String author = binding.qcAuthorEt.getText().toString().trim();
        String text = binding.qcTextEt.getText().toString().trim();
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String pageNumber = binding.qcPageNumberEt.getText().toString().trim();
        return new Quote(title, author, text, data, pageNumber);
    }

    private void createSnackbar(int id) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params.topMargin = 15;
        Snackbar snackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_SHORT);
        snackbar.getView().setLayoutParams(params);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.show_snackbar, null);
        TextView message = customView.findViewById(R.id.ss_text);
        message.setText(getString(id));
        snackbar.getView().setBackgroundResource(R.drawable.empty_drawable);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.addView(customView, 0);
        snackbar.show();
    }
}