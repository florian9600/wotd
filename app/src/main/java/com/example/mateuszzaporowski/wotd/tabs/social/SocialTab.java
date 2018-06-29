package com.example.mateuszzaporowski.wotd.tabs.social;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mateuszzaporowski.wotd.MainActivity;
import com.example.mateuszzaporowski.wotd.R;
import com.example.mateuszzaporowski.wotd.database.Article;
import com.example.mateuszzaporowski.wotd.database.ArticleHandler;
import com.example.mateuszzaporowski.wotd.support.Settings;
import com.example.mateuszzaporowski.wotd.support.Transition;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mateuszzaporowski on 12.05.18.
 */

@SuppressLint("ValidFragment")
public class SocialTab extends Fragment {

    MainActivity mainActivity;

    @SuppressLint("ValidFragment")
    public SocialTab (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mainActivity.renameStatusBar(getResources().getString(R.string.social));
        final View socialView = inflater.inflate(R.layout.social_view, container, false);

        ((TextView)socialView.findViewById(R.id.label_1)).setText(R.string.social_actual_streak);
        ((TextView)socialView.findViewById(R.id.label_2)).setText(R.string.social_full_weeks);
        ((TextView)socialView.findViewById(R.id.label_3)).setText(R.string.social_completed);
        ((TextView)socialView.findViewById(R.id.label_4)).setText(R.string.social_skipped_days);

        ((TextView)socialView.findViewById(R.id.friends)).setText(R.string.social_friends);
        ((TextView)socialView.findViewById(R.id.achievements)).setText(R.string.social_achievements);

        ArticleHandler handler = new ArticleHandler(socialView.getContext(), null);
        Settings settings = new Settings(mainActivity);
        ArrayList<Article> articles = handler.findArticles(settings.languagesValue[settings.getLanguages()], null);

        TextView stat;
        int actualStreak, fullWeeks, completed, skippedDays;
        actualStreak = fullWeeks = completed = skippedDays = 0;

        ArrayList<Article> reversedArticles = new ArrayList<Article>(articles);
        Collections.reverse(reversedArticles);
        for (Article article: reversedArticles) {
            if (article.getRead() == 1) {
                actualStreak++;
            } else {
                break;
            }
        }
        stat = socialView.findViewById(R.id.stat_1);
        stat.setText(String.format("%d", actualStreak));

        for (Article article: articles) {
            if (article.getRead() == 1) {
                completed++;
            } else {
                skippedDays++;
            }
        }
        stat = socialView.findViewById(R.id.stat_3);
        stat.setText(String.format("%d", completed));
        stat = socialView.findViewById(R.id.stat_4);
        stat.setText(String.format("%d", skippedDays));

        boolean completedWeek = false;
        for (Article article: articles) {
            if (article.getDate().getDayOfWeek() == DateTimeConstants.MONDAY) {
                if (completedWeek) {
                    fullWeeks++;
                }
                completedWeek = true;
            }
            if (article.getRead() == 0) {
                completedWeek = false;
            }
        }

        stat = socialView.findViewById(R.id.stat_2);
        stat.setText(String.format("%d", fullWeeks));

        TextView tvAchievements = socialView.findViewById(R.id.achievements);
        tvAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Achievements achievements = new Achievements(mainActivity);
                Transition.makeATransition(getFragmentManager(), achievements, "social");
            }
        });

        return socialView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
