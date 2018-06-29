package com.example.mateuszzaporowski.wotd.tabs.home;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mateuszzaporowski.wotd.MainActivity;
import com.example.mateuszzaporowski.wotd.R;
import com.example.mateuszzaporowski.wotd.database.ArticleHandler;
import com.example.mateuszzaporowski.wotd.support.Settings;
import com.example.mateuszzaporowski.wotd.support.TextRowAdapter;
import com.example.mateuszzaporowski.wotd.support.Transition;

import java.util.ArrayList;

/**
 * Created by mateuszzaporowski on 05.05.18.
 */

@SuppressLint("ValidFragment")
public class HomeTab extends ListFragment {
    ListView list;
    String[] text = new String[3];
    private FragmentManager fm;
    private Settings settings;

    com.example.mateuszzaporowski.wotd.database.Article wotd;
    MainActivity mainActivity;

    @SuppressLint("ValidFragment")
    public HomeTab(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        settings = new Settings(mainActivity);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        fm = getFragmentManager();
        mainActivity.renameStatusBar(getResources().getString(R.string.appname));

        text[0] = getResources().getString(R.string.home_favoutires);
        text[1] = getResources().getString(R.string.home_history);
        text[2] = getResources().getString(R.string.home_search);

        final View listmenuView = inflater.inflate(R.layout.menu_view, container, false);

        ArticleHandler handler = new ArticleHandler(listmenuView.getContext(), null);
        ArrayList<com.example.mateuszzaporowski.wotd.database.Article> articles = handler.findArticles(settings.languagesValue[settings.getLanguages()], null);
        wotd = articles.get(articles.size() - 1);

        TextView textViewWotd = listmenuView.findViewById(R.id.wotd);
        textViewWotd.setText(wotd.getWord());
        textViewWotd.setOnClickListener(view -> Transition.makeATransition(fm, new Article(wotd, mainActivity), "home"));

        list = listmenuView.findViewById(android.R.id.list);
        TextRowAdapter textRowAdapter = new TextRowAdapter(getActivity() , text );
        list.setAdapter(textRowAdapter);
        setListAdapter(textRowAdapter);

        return listmenuView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        ArticleHandler handler = new ArticleHandler(getActivity(), null);
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new Search(mainActivity, handler.findArticles(settings.languagesValue[settings.getLanguages()], 1), R.string.home_favoutires);
                break;
            case 1:
                fragment = new History(mainActivity);
                break;
            case 2:
                fragment = new Search(mainActivity, handler.findArticles(settings.languagesValue[settings.getLanguages()], null), R.string.home_search);
        }

        Transition.makeATransition(fm, fragment, "home");
    }
}
