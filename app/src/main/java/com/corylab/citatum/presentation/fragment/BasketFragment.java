package com.corylab.citatum.presentation.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corylab.citatum.CustomSnackBar;
import com.corylab.citatum.R;
import com.corylab.citatum.data.model.Quote;
import com.corylab.citatum.databinding.FragmentBasketBinding;
import com.corylab.citatum.presentation.activity.MainActivity;
import com.corylab.citatum.presentation.adapter.DeleteQuoteAdapter;
import com.corylab.citatum.presentation.viewmodel.QuoteViewModel;
import com.google.android.material.snackbar.Snackbar;

public class BasketFragment extends Fragment {

    private FragmentBasketBinding binding;
    private MainActivity activity;
    private QuoteViewModel quoteViewModel;

    public BasketFragment() {
        super(R.layout.fragment_basket);
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
        quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBasketBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        binding.bfRv.setLayoutManager(layoutManager);
        DeleteQuoteAdapter quoteAdapter = new DeleteQuoteAdapter();
        binding.bfRv.setAdapter(quoteAdapter);
        quoteViewModel.getRemovedQuotes().observe(getViewLifecycleOwner(), quotes -> quoteAdapter.submitList(quotes));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = 0;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Quote quote = new Quote(quoteAdapter.getQuoteAtPosition(position));
                if (direction == ItemTouchHelper.LEFT) {
                    quoteViewModel.delete(quote);
                    createUndoSnackbar(getView(), quote);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    quote.setRemovedFlag(0);
                    quoteViewModel.update(quote);
                    CustomSnackBar.createSnackbar(getView(), activity, R.string.qc_quotes_update);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                float alpha = 1.0f - Math.abs(dX) / itemView.getWidth();
                Paint paint = new Paint();
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX > 0) {
                        paint.setColor(ContextCompat.getColor(activity, R.color.light_blue));
                        paint.setAlpha((int) (alpha * 255));
                        float cornerRadius = 20f;
                        RectF rect = new RectF(itemView.getLeft(), itemView.getTop(), dX + itemView.getRight() / 2, itemView.getBottom());
                        c.drawRoundRect(rect, cornerRadius, cornerRadius, paint);

                        Drawable returnIcon = ContextCompat.getDrawable(activity, R.drawable.icon_return_bt);
                        Drawable.ConstantState constantState = returnIcon.mutate().getConstantState();
                        Drawable drawableCopy = constantState.newDrawable();
                        drawableCopy.setTint(ContextCompat.getColor(activity, R.color.background_color));
                        int iconWidth = drawableCopy.getIntrinsicWidth();
                        int iconHeight = drawableCopy.getIntrinsicHeight();
                        int iconTop = itemView.getTop() + (itemView.getHeight() - iconHeight) / 2;
                        int iconMargin = (itemView.getHeight() - iconHeight) / 5;
                        int iconLeft = itemView.getLeft() + iconMargin;
                        int iconRight = itemView.getLeft() + iconMargin + iconWidth;
                        int iconBottom = iconTop + iconHeight;
                        drawableCopy.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        drawableCopy.draw(c);

                    } else if (dX < 0) {
                        paint.setColor(ContextCompat.getColor(activity, R.color.light_red));
                        paint.setAlpha((int) (alpha * 255));
                        float cornerRadius = 20f;
                        RectF rect = new RectF(itemView.getRight() - itemView.getRight() / 2 + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        c.drawRoundRect(rect, cornerRadius, cornerRadius, paint);

                        Drawable basketIcon = ContextCompat.getDrawable(activity, R.drawable.icon_basket);
                        Drawable.ConstantState constantState = basketIcon.mutate().getConstantState();
                        Drawable drawableCopy = constantState.newDrawable();
                        drawableCopy.setTint(ContextCompat.getColor(activity, R.color.background_color));
                        int iconWidth = drawableCopy.getIntrinsicWidth();
                        int iconHeight = drawableCopy.getIntrinsicHeight();
                        int iconTop = itemView.getTop() + (itemView.getHeight() - iconHeight) / 2;
                        int iconMargin = (itemView.getHeight() - iconHeight) / 5;
                        int iconLeft = itemView.getRight() - iconMargin - iconWidth;
                        int iconRight = itemView.getRight() - iconMargin;
                        int iconBottom = iconTop + iconHeight;
                        drawableCopy.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        drawableCopy.draw(c);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(binding.bfRv);
    }

    private void createUndoSnackbar(View view, Quote quote) {
        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.show_undo_snackbar, null);
        TextView message = customView.findViewById(R.id.sus_text);
        message.setText(getString(R.string.sus_quote_delete_text));
        Button undoButton = customView.findViewById(R.id.sus_undo_text);
        undoButton.setOnClickListener(view1 -> {
            quoteViewModel.insert(quote);
            undoButton.setOnClickListener(null);
        });
        snackbar.getView().setBackgroundResource(R.drawable.empty_drawable);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.addView(customView, 0);
        snackbar.show();
    }
}
