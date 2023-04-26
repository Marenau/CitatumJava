package com.corylab.citatum.presentation.fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corylab.citatum.R;
import com.corylab.citatum.data.model.Quote;
import com.corylab.citatum.data.model.Tag;
import com.corylab.citatum.databinding.FragmentQuoteCreateBinding;
import com.corylab.citatum.presentation.adapter.QuoteTagAdapter;
import com.corylab.citatum.presentation.viewmodel.QuoteTagJoinViewModel;
import com.corylab.citatum.presentation.viewmodel.QuoteViewModel;
import com.corylab.citatum.presentation.activity.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QuoteCreateFragment extends Fragment {

    private FragmentQuoteCreateBinding binding;
    private MainActivity activity;
    private QuoteViewModel quoteViewModel;
    private QuoteTagJoinViewModel quoteTagJoinViewModel;

    private Quote thisQuote = new Quote();

    public QuoteCreateFragment() {
        super(R.layout.fragment_quote_create);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        activity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("uid", thisQuote.getUid());
        outState.putString("title", thisQuote.getTitle());
        outState.putString("author", thisQuote.getAuthor());
        outState.putString("text", thisQuote.getText());
        outState.putString("date", thisQuote.getDate());
        outState.putString("page_number", thisQuote.getPageNumber());
        outState.putInt("bookmark_flag", thisQuote.getBookmarkFlag());
        outState.putInt("remove_flag", thisQuote.getRemovedFlag());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            thisQuote.setUid(savedInstanceState.getInt("uid"));
            thisQuote.setTitle(savedInstanceState.getString("title"));
            thisQuote.setAuthor(savedInstanceState.getString("author"));
            thisQuote.setText(savedInstanceState.getString("text"));
            thisQuote.setDate(savedInstanceState.getString("date"));
            thisQuote.setPageNumber(savedInstanceState.getString("page_number"));
            thisQuote.setBookmarkFlag(savedInstanceState.getInt("bookmark_flag"));
            thisQuote.setRemovedFlag(savedInstanceState.getInt("remove_flag"));
        }

        quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
        quoteTagJoinViewModel = new ViewModelProvider(this).get(QuoteTagJoinViewModel.class);
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
        Bundle trBundle = getArguments();
        if (trBundle != null) {
            if (trBundle.containsKey("uid")) {
                int uid = trBundle.getInt("uid");
                thisQuote = quoteViewModel.getQuoteByUid(uid);
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

        binding.qcTitleEt.setText(thisQuote.getTitle());
        binding.qcAuthorEt.setText(thisQuote.getAuthor());
        binding.qcTextEt.setText(thisQuote.getText());
        binding.qcPageNumberEt.setText(thisQuote.getPageNumber());
        if (thisQuote.getBookmarkFlag() == 1) {
            setBookmarkColor(R.color.light_red);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        binding.qcTagsRv.setLayoutManager(layoutManager);
        QuoteTagAdapter adapter = new QuoteTagAdapter();
        binding.qcTagsRv.setAdapter(adapter);
        quoteTagJoinViewModel.getTagsForQuote(thisQuote.getUid()).observe(getViewLifecycleOwner(), tags -> adapter.submitList(tags));

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.image_scale);

        binding.qcConfirmIcon.setOnClickListener(view -> {
            binding.qcConfirmIcon.startAnimation(animation);
            updateQuote();
            if (thisQuote.getUid() == -1) {
                quoteViewModel.insert(thisQuote);
                thisQuote = quoteViewModel.getQuoteByUid(quoteViewModel.getMaxId());
                createSnackbar(R.string.qc_quotes_added);
            } else {
                quoteViewModel.update(thisQuote);
                createSnackbar(R.string.qc_quotes_update);
            }
            InputMethodManager inputMethodManagerHide = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManagerHide.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        binding.qcTagIcon.setOnClickListener(view -> {
            binding.qcTagIcon.startAnimation(animation);
            if (thisQuote.getUid() != -1) {
                updateQuote();
                Bundle transfer = new Bundle();
                transfer.putInt("uid", thisQuote.getUid());
                Log.i("ID", String.valueOf(thisQuote.getUid()));
                Navigation.findNavController(view).navigate(R.id.action_quoteCreateFragment_to_tagSelectionFragment, transfer);
            } else {
                createSnackbar(R.string.qc_add_quote_request);
            }
        });

        binding.qcBookmarkIcon.setOnClickListener(view -> {
            binding.qcBookmarkIcon.startAnimation(animation);
            if (thisQuote.getUid() != -1) {
                updateQuote();
                if (thisQuote.getBookmarkFlag() == 0) {
                    setBookmarkColor(R.color.light_red);
                    thisQuote.setBookmarkFlag(1);
                    quoteViewModel.update(thisQuote);
                } else {
                    setBookmarkColor(R.color.black);
                    thisQuote.setBookmarkFlag(0);
                    quoteViewModel.update(thisQuote);
                }
            } else {
                createSnackbar(R.string.qc_add_quote_request);
            }
        });

        binding.qcBasketIcon.setOnClickListener(view -> {
            binding.qcBasketIcon.startAnimation(animation);
            if (thisQuote.getUid() != -1) {
                updateQuote();
                thisQuote.setRemovedFlag(1);
                quoteViewModel.update(thisQuote);
                createUndoSnackbar();
                Navigation.findNavController(view).navigateUp();
            } else {
                createSnackbar(R.string.qc_add_quote_request);
            }
        });
    }

    private void setBookmarkColor(int id) {
        binding.qcBookmarkIcon.getDrawable().setColorFilter(
                new PorterDuffColorFilter(getResources().getColor(id, activity.getTheme()),
                        PorterDuff.Mode.SRC_IN));
    }

    private void updateQuote() {
        binding.qcTitleEt.setText(binding.qcTitleEt.getText().toString().isEmpty() ?
                getString(R.string.qc_autosubstitution_title_text) : binding.qcTitleEt.getText().toString());
        binding.qcAuthorEt.setText(binding.qcAuthorEt.getText().toString().isEmpty() ?
                getString(R.string.qc_autosubstitution_author_text) : binding.qcAuthorEt.getText().toString());
        binding.qcTextEt.setText(binding.qcTextEt.getText().toString().isEmpty() ?
                getString(R.string.qc_autosubstitution_text_text) : binding.qcTextEt.getText().toString());
        binding.qcPageNumberEt.setText(binding.qcPageNumberEt.getText().toString().isEmpty() ?
                getString(R.string.qc_autosubstitution_page_number_text) : binding.qcPageNumberEt.getText().toString());
        thisQuote.setTitle(binding.qcTitleEt.getText().toString().trim());
        thisQuote.setAuthor(binding.qcAuthorEt.getText().toString().trim());
        thisQuote.setText(binding.qcTextEt.getText().toString().trim());
        thisQuote.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        thisQuote.setPageNumber(binding.qcPageNumberEt.getText().toString().trim());
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

    private void createUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_SHORT);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.show_undo_snackbar, null);
        TextView message = customView.findViewById(R.id.sus_text);
        message.setText(getString(R.string.sus_quote_remove_text));
        Button undoButton = customView.findViewById(R.id.sus_undo_text);
        undoButton.setOnClickListener(view -> {
            thisQuote.setRemovedFlag(0);
            quoteViewModel.update(thisQuote);
            undoButton.setOnClickListener(null);
        });
        snackbar.getView().setBackgroundResource(R.drawable.empty_drawable);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.addView(customView, 0);
        snackbar.show();
    }
}