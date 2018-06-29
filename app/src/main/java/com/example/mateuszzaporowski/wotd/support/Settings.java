package com.example.mateuszzaporowski.wotd.support;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mateuszzaporowski.wotd.MainActivity;
import com.example.mateuszzaporowski.wotd.R;

/**
 * Created by mateuszzaporowski on 29.05.18.
 */

public class Settings {
    private Context ma;

    public Settings (Context ma) {
        this.ma = ma;
    }

    private String path = "com.example.mateuszzaporowski.wotd";

    public int[] fontSizeValue = {30, 36, 42, 48};
    public String[] languagesValue = {"FK", "EN", "EN", "EN"};
    public int[] notificationsValue = {0, 1, 2, 3, 7};

    public int[] fontSizeDesc = {R.string.fontsize_small, R.string.fontsize_normal,
                                    R.string.fontsize_large, R.string.fontsize_very_large};
    public int[] languagesDesc = {R.string.language_lorem, R.string.language_english,
                                    R.string.language_polish, R.string.language_german};
    public int[] notificationsDesc = {R.string.notification_0, R.string.notification_1,
                                    R.string.notification_2, R.string.notification_3,
                                    R.string.notification_7};

    private String fontSizePath = ".fontsize";
    private String languagesPath = ".languages";
    private String notificationsPath = ".notifications";

    public void setFontSize (int value) {
        setSharedPreference(value, fontSizePath);
    }

    public void setLanguages (int value) {
        NotificationManager.setUpNotification(ma);
        setSharedPreference(value, languagesPath);
    }

    public void setNotifications (int value) {
        NotificationManager.setUpNotification(ma);
        setSharedPreference(value, notificationsPath);
    }

    private void setSharedPreference (int value, String location) {
        SharedPreferences prefs = ma.getSharedPreferences(
                path, android.content.Context.MODE_PRIVATE);

        prefs.edit().putInt(location, value).apply();
    }

    public int getFontSize () {
        return getSharedPreference(fontSizePath);
    }

    public int getLanguages () {
        return getSharedPreference(languagesPath);
    }

    public int getNotifications () {
        return getSharedPreference(notificationsPath);
    }

    private int getSharedPreference (String location) {
        SharedPreferences prefs = ma.getSharedPreferences(
                path, android.content.Context.MODE_PRIVATE);

        return prefs.getInt(location, 0);
    }
}
