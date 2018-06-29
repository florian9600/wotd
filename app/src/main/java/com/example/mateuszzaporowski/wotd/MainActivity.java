package com.example.mateuszzaporowski.wotd;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mateuszzaporowski.wotd.database.Article;
import com.example.mateuszzaporowski.wotd.database.ArticleHandler;
import com.example.mateuszzaporowski.wotd.support.ActionBarTextView;
import com.example.mateuszzaporowski.wotd.support.NotificationManager;
import com.example.mateuszzaporowski.wotd.support.Settings;
import com.example.mateuszzaporowski.wotd.tabs.home.HomeTab;
import com.example.mateuszzaporowski.wotd.tabs.settings.SettingsTab;
import com.example.mateuszzaporowski.wotd.tabs.social.SocialTab;

import java.io.Serializable;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements Serializable {

    private ImageView[] navButtons;

    HomeTab homeTab;
    SocialTab socialTab;
    SettingsTab settingsTab;

    static public int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        NotificationManager.setUpNotification(this);

        LinearLayout linearLayout = findViewById(R.id.content);
        linearLayout.post(new Runnable() {
            @Override
            public void run() {
                width = linearLayout.getWidth();
            }
        });

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(0);

        navButtons = new ImageView[3];
        navButtons[0] = findViewById(R.id.btn_home);
        navButtons[1] = findViewById(R.id.btn_social);
        navButtons[2] = findViewById(R.id.btn_settings);

        homeTab = new HomeTab(this);
        socialTab = new SocialTab(this);
        settingsTab = new SettingsTab(this);

        switchTab(findViewById(R.id.btn_home));
    }

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void switchTab(View view) {
        for (ImageView button: navButtons) {
            button.setColorFilter( 0xffbbbdbd, PorterDuff.Mode.MULTIPLY );
        }
        ImageView iv = (ImageView) view;
        iv.setColorFilter( 0xff00d3f8, PorterDuff.Mode.MULTIPLY );

        switch (view.getId()) {
            case R.id.btn_home:
                pushFragment(homeTab);
                return;
            case R.id.btn_social:
                pushFragment(socialTab);
                return;
            case R.id.btn_settings:
                pushFragment(settingsTab);
                return;
        }
    }

    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                android.app.FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(
                        0);
                fragmentManager.popBackStack(entry.getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.executePendingTransactions();
            }



            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.content, fragment);
                ft.commit();
            }
        }
    }

    public void renameStatusBar(String newName) {
        View view = this.getSupportActionBar().getCustomView();
        ActionBarTextView actionBar = view.findViewById(R.id.custom_action_bar_title);
        actionBar.setTextFixed(newName);
    }
}
