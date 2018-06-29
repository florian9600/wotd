package com.example.mateuszzaporowski.wotd.tabs.social;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mateuszzaporowski.wotd.MainActivity;
import com.example.mateuszzaporowski.wotd.R;
import com.example.mateuszzaporowski.wotd.database.Achievement;
import com.example.mateuszzaporowski.wotd.database.AchievementHandler;
import com.example.mateuszzaporowski.wotd.support.Settings;

import java.util.ArrayList;

/**
 * Created by mateuszzaporowski on 04.06.18.
 */

@SuppressLint("ValidFragment")
public class Achievements extends ListFragment {
    private MainActivity mainActivity;

    @SuppressLint("ValidFragment")
    public Achievements(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity.renameStatusBar(getResources().getString(R.string.achievements));

        View achievementsView = inflater.inflate(R.layout.achievements_view, container, false);

        AchievementHandler achievementHandler = new AchievementHandler(
                achievementsView.getContext(), null, getResources());

        Settings settings = new Settings(achievementsView.getContext());
        achievementHandler.checkForNewAchievements(settings.languagesValue[settings.getLanguages()]);

        ListView listView = achievementsView.findViewById(android.R.id.list);

        AchievementHandler handler = new AchievementHandler(achievementsView.getContext(), null, getResources());
        ArrayList<Achievement> achievements = handler.getAchievements();

        AchievementsRowAdapter ara = new AchievementsRowAdapter(mainActivity,
                achievements.toArray(new Achievement[achievements.size()]));

        listView.setAdapter(ara);
        setListAdapter(ara);

        return achievementsView;
    }
}
