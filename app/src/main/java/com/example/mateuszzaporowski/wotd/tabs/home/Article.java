package com.example.mateuszzaporowski.wotd.tabs.home;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mateuszzaporowski.wotd.MainActivity;
import com.example.mateuszzaporowski.wotd.R;
import com.example.mateuszzaporowski.wotd.database.AchievementHandler;
import com.example.mateuszzaporowski.wotd.database.ArticleHandler;
import com.example.mateuszzaporowski.wotd.database.Translation;
import com.example.mateuszzaporowski.wotd.support.Settings;

import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by mateuszzaporowski on 06.05.18.
 */

@SuppressLint("ValidFragment")
public class Article extends Fragment {
    com.example.mateuszzaporowski.wotd.database.Article article;
    MainActivity mainActivity;
    ImageView star;
    TextToSpeech tts;

    @SuppressLint("ValidFragment")
    public Article (com.example.mateuszzaporowski.wotd.database.Article article, MainActivity mainActivity) {
        this.article = article;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // change the status on the action bar
        mainActivity.renameStatusBar(article.getWord());

        // get the view from .xml
        View articleView = inflater.inflate(R.layout.article, container, false);

        // set content of the article and the font size
        TextView content = articleView.findViewById(R.id.content);
        content.setText(article.getContent());
        Settings settings = new Settings(mainActivity);
        content.setTextSize(COMPLEX_UNIT_SP, settings.fontSizeValue[settings.getFontSize()]);

        // set title of the "synthesize a word" button
        TextView tvSynthesize1 = articleView.findViewById(R.id.read_button1);
        tvSynthesize1.setText(article.getWord());

        // set date and pronunciation
        TextView textView = articleView.findViewById(R.id.pronunciation);
        textView.setText(article.getPronunciation());
        textView = articleView.findViewById(R.id.date);
        textView.setText(article.getDate().toString(DateTimeFormat.forPattern("d.MM.yyyy")));

        // make the star work (marking as favorite)
        View actionBar = mainActivity.getSupportActionBar().getCustomView();
        star = actionBar.findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                article.swapFavorite();
                updateStar();
                ArticleHandler handler = new ArticleHandler(getActivity(), null);
                handler.updateArticle(article);
            }
        });
        updateStar();
        star.setVisibility(View.VISIBLE);

        // set up of synthesizing the speech
        tts = new TextToSpeech(articleView.getContext(), null);
        tts.setLanguage(Locale.UK);
        // the word of the article (WOTD)
        tvSynthesize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(article.getWord(), TextToSpeech.QUEUE_ADD, null, new String());
            }
        });
        // the whole article
        TextView tvSynthesize2 = articleView.findViewById(R.id.read_button2);
        tvSynthesize2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(article.getContent(), TextToSpeech.QUEUE_ADD, null, new String());
            }
        });

        ArticleHandler handler = new ArticleHandler(articleView.getContext(), null);
        Translation translation = handler.getTranslation(article.getTranslation());
        String correctTraslation = translation.get(Locale.getDefault().getLanguage());

        EditText editText = articleView.findViewById(R.id.guess);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))){
                    String comment = getResources().getString(R.string.guess_false);
                    if (correctTraslation.compareToIgnoreCase(v.getText().toString()) == 0) {
                        comment = getResources().getString(R.string.guess_correct);
                        article.setReadTrue();
                        handler.updateArticle(article);
                    }
                    Toast.makeText(mainActivity,
                            comment,
                            Toast.LENGTH_SHORT)
                            .show();
                    return true;
                } else {
                    return false;
                }
            }
        });

        return articleView;
    }

    @Override
    // when the article starts to dissappearing, the star should too
    public void onPause() {
        super.onPause();
        star.setVisibility(View.GONE);
    }

    // update the image of the star accordingly to whether or not the article is marked as favorite
    private void updateStar() {
        int image = article.getFavorite() == 1 ? R.drawable.star_filled : R.drawable.star_unfilled;
        star.setImageResource(image);
    }
}
