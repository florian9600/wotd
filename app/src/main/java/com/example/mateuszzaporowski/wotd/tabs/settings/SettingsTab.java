package com.example.mateuszzaporowski.wotd.tabs.settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mateuszzaporowski.wotd.MainActivity;
import com.example.mateuszzaporowski.wotd.R;
import com.example.mateuszzaporowski.wotd.database.AchievementHandler;
import com.example.mateuszzaporowski.wotd.database.ArticleHandler;
import com.example.mateuszzaporowski.wotd.support.Settings;
import com.example.mateuszzaporowski.wotd.support.TextRowAdapter;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

/**
 * Created by mateuszzaporowski on 12.05.18.
 */

@SuppressLint("ValidFragment")
public class SettingsTab extends ListFragment {
    MainActivity mainActivity;
    String[] text = new String[3];
    Settings settings;
    View settingsView;
    SettingsTab st = this;

    @SuppressLint("ValidFragment")
    public SettingsTab (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        settings = new Settings(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity.renameStatusBar(getResources().getString(R.string.settings));
        settingsView = inflater.inflate(R.layout.settings_view, container, false);

        text[0] = getResources().getString(R.string.settings_font_size);
        text[1] = getResources().getString(R.string.settings_learned_language);
        text[2] = getResources().getString(R.string.settings_notifications);

        ListView listView = settingsView.findViewById(android.R.id.list);
        TextRowAdapter textRowAdapter = new TextRowAdapter(getActivity() , text );

        String[] additionalText = new String[3];
        additionalText[0] = getResources().getString(settings.fontSizeDesc[settings.getFontSize()]);
        additionalText[1] = getResources().getString(settings.languagesDesc[settings.getLanguages()]);
        additionalText[2] = getResources().getString(settings.notificationsDesc[settings.getNotifications()]);
        textRowAdapter.set_alternativeText(additionalText);

        listView.setAdapter(textRowAdapter);
        setListAdapter(textRowAdapter);

        TextView resetCounter = settingsView.findViewById(R.id.reset_counter);
        resetCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                ArticleHandler articleHandler = new ArticleHandler(settingsView.getContext(), null);
                                articleHandler.updateAllArticlesToUnread();
                                articleHandler.updateAllArticlesToNotFavorite();
                                AchievementHandler achievementHandler = new AchievementHandler(
                                        settingsView.getContext(), null, getResources()
                                );
                                achievementHandler.updateAllAchievementsToUndone();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(settingsView.getContext());
                builder.setMessage(R.string.settings_reset_counter_dialog).setPositiveButton(R.string.dialogYes, dialogClickListener)
                        .setNegativeButton(R.string.dialogNo, dialogClickListener).show();
            }
        });

        return settingsView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String title = null;
        int[] options = null;
        int selected = 0;
        String saveChangesMethodName = null;
        Method saveChanges = null;
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = int.class;

        switch (position) {
            case 0:
                title = getResources().getString(R.string.settings_font_size);
                options = settings.fontSizeDesc;
                selected = settings.getFontSize();
                saveChangesMethodName = "setFontSize";
                break;
            case 1:
                title = getResources().getString(R.string.settings_learned_language);
                options = settings.languagesDesc;
                selected = settings.getLanguages();
                saveChangesMethodName = "setLanguages";
                break;
            default:
                title = getResources().getString(R.string.settings_notifications);
                options = settings.notificationsDesc;
                selected = settings.getNotifications();
                saveChangesMethodName = "setNotifications";
                break;
        }

        String[] optionsString = new String[options.length];
        for (int i = 0; i < options.length; i++) {
            optionsString[i] = getResources().getString(options[i]);
        }

        try {
            saveChanges = Settings.class.getMethod(saveChangesMethodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        showDialog(settingsView.getContext(), title, optionsString, saveChanges, settings, selected);
    }

    public void showDialog(android.content.Context context, String title, String[] items, Method saveChanges, Object object, int selected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        builder.setSingleChoiceItems(items, selected,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Object[] parameters = new Object[1];
                        parameters[0] = item;
                        try {
                            saveChanges.invoke(object, parameters);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        FragmentTransaction ftr = mainActivity.getFragmentManager().beginTransaction();
                        ftr.detach(st).attach(st).commit();

                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
