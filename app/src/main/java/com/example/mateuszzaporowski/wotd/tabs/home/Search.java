package com.example.mateuszzaporowski.wotd.tabs.home;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mateuszzaporowski.wotd.MainActivity;
import com.example.mateuszzaporowski.wotd.R;
import com.example.mateuszzaporowski.wotd.database.Article;
import com.example.mateuszzaporowski.wotd.support.TextRowAdapter;
import com.example.mateuszzaporowski.wotd.support.Transition;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mateuszzaporowski on 12.05.18.
 */

@SuppressLint("ValidFragment")
public class Search extends ListFragment {
    View searchView;
    ArrayList<com.example.mateuszzaporowski.wotd.database.Article> articles;
    ArrayList<com.example.mateuszzaporowski.wotd.database.Article> currentArticles;
    ListView listView;
    MainActivity mainActivity;
    int statusBarName;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @SuppressLint("ValidFragment")
    public Search (MainActivity mainActivity, ArrayList<com.example.mateuszzaporowski.wotd.database.Article> articles, int statusBarName) {
        this.mainActivity = mainActivity;
        this.articles = articles;
        this.statusBarName = statusBarName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity.renameStatusBar(getResources().getString(statusBarName));

        searchView = inflater.inflate(R.layout.search_view, container, false);

        EditText searchField = searchView.findViewById(R.id.search_field);
        searchField.setHint(R.string.search_field);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Article> goodOnes = new ArrayList<Article>();
                for (Article article: articles) {
                    if (article.getWord().toLowerCase().matches(charSequence.toString().toLowerCase() + ".*")) {
                        goodOnes.add(article);
                    }
                }

                display(goodOnes);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                display(articles);
            }
        });
        return searchView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void display (ArrayList<com.example.mateuszzaporowski.wotd.database.Article> articles) {
        currentArticles = new ArrayList<com.example.mateuszzaporowski.wotd.database.Article>(articles);

        String[] words = new String[articles.size()];
        for (int i = 0; i < articles.size(); i++) {
            words[i] = articles.get(i).getWord();
        }

        listView = searchView.findViewById(android.R.id.list);
        listView.setAdapter(new TextRowAdapter(getActivity(), words));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Transition.makeATransition(
                getFragmentManager(),
                new com.example.mateuszzaporowski.wotd.tabs.home.Article(currentArticles.get(position), mainActivity), "home");
    }
}
