package com.example.mateuszzaporowski.wotd.tabs.home;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mateuszzaporowski.wotd.MainActivity;
import com.example.mateuszzaporowski.wotd.R;
import com.example.mateuszzaporowski.wotd.database.*;
import com.example.mateuszzaporowski.wotd.support.Circle;
import com.example.mateuszzaporowski.wotd.support.Settings;
import com.example.mateuszzaporowski.wotd.support.Transition;

import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

/**
 * Created by mateuszzaporowski on 12.05.18.
 */

@SuppressLint("ValidFragment")
public class History extends Fragment {
    MainActivity mainActivity;

    @SuppressLint("ValidFragment")
    public History(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mainActivity.renameStatusBar(getResources().getString(R.string.home_history));

        View historyView = inflater.inflate(R.layout.history_view, container, false);
        LinearLayout linearLayout = historyView.findViewById(R.id.contentOfScrollView);

        ArticleHandler handler = new ArticleHandler(getActivity(), null);
        Settings settings = new Settings(mainActivity);
        ArrayList<com.example.mateuszzaporowski.wotd.database.Article> articles = handler.findArticles(settings.languagesValue[settings.getLanguages()], null);

        final double usedPart = 10.f / 11.f;
        final int usedWidth = (int) Math.round(mainActivity.width * usedPart);
        final int buttonSize = usedWidth / 7;
        final int marginOfCircle = buttonSize / 8;
        final int circleSize = buttonSize - 2 * marginOfCircle;
        final int marginOfMonth = (int) ((1 - usedPart) * usedWidth / 2 + marginOfCircle);
        final int monthColor = Color.argb(255, 242, 55, 18);
        final int readColor = Color.argb(255, 133, 195, 93);
        final int notReadColor = Color.BLACK;
        final int firstDayOfTheWeek = DateTimeConstants.MONDAY;
        final Typeface monthFont = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/Arial.ttf");

        boolean firstIteration = true;
        TextView monthRow = null;
        LinearLayout weekRow = null;
        for (com.example.mateuszzaporowski.wotd.database.Article article: articles) {
            if (firstIteration || article.getDate().getDayOfMonth() == 1) {
                monthRow = new TextView(historyView.getContext());
                monthRow.setText(article.getDate().toString(DateTimeFormat.forPattern("MMMM")) + ":");
                monthRow.setTextColor(monthColor);
                monthRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36.f);
                monthRow.setTypeface(monthFont, Typeface.BOLD);
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(marginOfMonth, marginOfMonth / 2, 0, 0);
                monthRow.setLayoutParams(layoutParams);
                linearLayout.addView(monthRow);
                weekRow = null;
            }
            if (weekRow == null || article.getDate().getDayOfWeek() == firstDayOfTheWeek) {
                boolean firstWeekOfMonth = weekRow == null;
                weekRow = new LinearLayout(historyView.getContext());
                weekRow.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(usedWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.weight = 1.0f;
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                weekRow.setLayoutParams(layoutParams);
                int gravity = 0;
                if (firstIteration && article.getDate().getDayOfMonth() < 8) {
                    gravity = Gravity.END;
                } else {
                    gravity = firstWeekOfMonth ? Gravity.END : Gravity.START;
                }
                weekRow.setGravity(gravity);
                linearLayout.addView(weekRow);
            }

            int color = article.getRead() == 1 ? readColor : notReadColor;

            Circle circle = new Circle(historyView.getContext(), circleSize / 2, color);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(circleSize, circleSize);
            layoutParams.setMargins(marginOfCircle, marginOfCircle,
                    marginOfCircle, marginOfCircle);
            circle.setLayoutParams(layoutParams);

            circle.setOnClickListener(view ->
                    Transition.makeATransition(
                        getFragmentManager(),
                        new com.example.mateuszzaporowski.wotd.tabs.home.Article(article, mainActivity), "home")
            );

            weekRow.addView(circle);
            firstIteration = false;
        }

        return historyView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
