package com.example.mateuszzaporowski.wotd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by mateuszzaporowski on 09.05.18.
 */

public class ArticleHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 23;
    private static final String DATABASE_NAME = "wotdDB.db";

    private static final String TABLE_ARTICLE = "article";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_WORD = "word";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_PRONUNCIATION = "pronunciation";
    private static final String COLUMN_LANGUAGE = "language";
    private static final String COLUMN_READ = "read";
    private static final String COLUMN_FAVORITE = "favorite";
    private static final String COLUMN_TRANSLATION = "translation";

    private static final String TABLE_TRANSLATION = "translation";
    private static final String COLUMN_ENGLISH = "english";
    private static final String COLUMN_GERMAN = "german";
    private static final String COLUMN_POLISH = "polish";

    private Context context;

    private String articleExample =
            "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor" +
            " invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero" +
            " eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren," +
            " no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor" +
            " sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt" +
            " ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et" +
            " accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea" +
            " takimata sanctus est Lorem ipsum dolor sit amet.";

    public ArticleHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ARTICLE_TABLE =
                "CREATE TABLE " + TABLE_ARTICLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_DATE + " INTEGER,"
                + COLUMN_WORD + " VARCHAR,"
                + COLUMN_CONTENT + " VARCHAR,"
                + COLUMN_PRONUNCIATION + " VARCHAR,"
                + COLUMN_LANGUAGE + " VARCHAR,"
                + COLUMN_READ + " INTEGER,"
                + COLUMN_FAVORITE + " INTEGER,"
                + COLUMN_TRANSLATION + " INTEGER"
                + ")";
        sqLiteDatabase.execSQL(CREATE_ARTICLE_TABLE);

        String CREATE_TRANSLATION_TABLE =
                "CREATE TABLE " + TABLE_TRANSLATION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_ENGLISH + " VARCHAR,"
                + COLUMN_GERMAN + " VARCHAR,"
                + COLUMN_POLISH + " VARCHAR"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TRANSLATION_TABLE);

        int i = 1;
        ArrayList<String> listOfWords = new ArrayList<String>(Arrays.asList(articleExample.split("[ ,.]")));
        listOfWords.removeAll(Arrays.asList("", null));
        for (String word: listOfWords) {
            Article example = new Article(
                    -1,
                    new LocalDate().minusDays(listOfWords.size() - i)
                            .toLocalDateTime(org.joda.time.LocalTime.MIDNIGHT)
                            .toDateTime(DateTimeZone.UTC).getMillis(),
                    word.substring(0, 1).toUpperCase() + word.substring(1),
                    articleExample,
                    "[ˈcɕɔ̃w̃ʃka]",
                    "FK",
                    ThreadLocalRandom.current().nextInt(0, 10) < 5 ?  0 : 1,
                    ThreadLocalRandom.current().nextInt(0, 10) < 1 ?  1 : 0,
                    1);

            addArticle(example, sqLiteDatabase);
            i++;
        }

        InputStream articlesIS = context.getResources().openRawResource(
                context.getResources().getIdentifier("articles_en",
                        "raw", "com.example.mateuszzaporowski.wotd"));

        InputStream translationsIS = context.getResources().openRawResource(
                context.getResources().getIdentifier("translations_en",
                        "raw", "com.example.mateuszzaporowski.wotd"));

        String articles_EN_CSV = null;
        String translations_EN_CSV = null;
        try {
            articles_EN_CSV = IOUtils.toString(articlesIS, (String) null);
            translations_EN_CSV = IOUtils.toString(translationsIS, (String) null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] articlesLines = articles_EN_CSV.split("\n");
        String[] translationsLines = translations_EN_CSV.split("\n");

        for (int d = 0; d < articlesLines.length; d++) {
            String[] articleSplit = articlesLines[d].split("\\|");
            String[] translationSplit = translationsLines[d].split(",");

            Translation translation = new Translation(translationSplit[0], translationSplit[1], translationSplit[2]);
            addTranslation(translation, sqLiteDatabase);

            Article article = new Article(
                    -1,
                    new LocalDate().minusDays(articlesLines.length - (d + 1))
                            .toLocalDateTime(org.joda.time.LocalTime.MIDNIGHT)
                            .toDateTime(DateTimeZone.UTC).getMillis(),
                    articleSplit[0],
                    articleSplit[1],
                    articleSplit[2],
                    articleSplit[3],
                    0,
                    0,
                    d + 1
            );
            addArticle(article, sqLiteDatabase);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATION);
        onCreate(sqLiteDatabase);
    }

    private void addArticle(Article article, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, article.getDateLong());
        values.put(COLUMN_WORD, article.getWord());
        values.put(COLUMN_CONTENT, article.getContent());
        values.put(COLUMN_PRONUNCIATION, article.getPronunciation());
        values.put(COLUMN_LANGUAGE, article.getLanguage());
        values.put(COLUMN_READ, article.getRead());
        values.put(COLUMN_FAVORITE, article.getFavorite());
        values.put(COLUMN_TRANSLATION, article.getTranslation());

        db.insert(TABLE_ARTICLE, null, values);
        //db.close();
    }

    private void addTranslation(Translation translation, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ENGLISH, translation.get("EN"));
        values.put(COLUMN_GERMAN, translation.get("DE"));
        values.put(COLUMN_POLISH, translation.get("PL"));

        db.insert(TABLE_TRANSLATION, null, values);
    }

    public Translation getTranslation(int id) {
        String query = "Select * FROM " + TABLE_TRANSLATION + " WHERE " + COLUMN_ID + " = \"" + id + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        Translation translation = new Translation(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );

        return translation;
    }

    public ArrayList<Article> findArticles(String language, Integer favorite) {
        String query = "Select * FROM " + TABLE_ARTICLE + " WHERE " + COLUMN_LANGUAGE + " =  \"" + language + "\"";

        if (favorite != null) {
            query += " AND " + COLUMN_FAVORITE + " = " + favorite;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Article> articles = new ArrayList<Article>();

        cursor.moveToFirst();
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            do {
                articles.add(new Article(
                        Integer.parseInt(cursor.getString(0)),
                        Long.parseLong(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        Integer.parseInt(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7)),
                        Integer.parseInt(cursor.getString(8))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return articles;
    }

    public void updateArticle(Article article) {
        String query = "UPDATE " + TABLE_ARTICLE + " SET "
                + COLUMN_DATE + " = '" + article.getDateLong() + "', "
                + COLUMN_WORD + " = '" + article.getWord() + "', "
                + COLUMN_CONTENT + " = '" + article.getContent().replace("'", "''") + "', "
                + COLUMN_PRONUNCIATION + " = '" + article.getPronunciation() + "', "
                + COLUMN_LANGUAGE + " = '" + article.getLanguage() + "', "
                + COLUMN_READ + " = '" + article.getRead() + "', "
                + COLUMN_FAVORITE + " = '" + article.getFavorite() + "', "
                + COLUMN_TRANSLATION + " = '" + article.getTranslation() + "' WHERE "
                + COLUMN_ID + " = '" + article.getId() + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    public void updateAllArticlesToUnread() {
        String query = "UPDATE " + TABLE_ARTICLE + " SET " + COLUMN_READ + " = '0';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    public void updateAllArticlesToNotFavorite() {
        String query = "UPDATE " + TABLE_ARTICLE + " SET " + COLUMN_FAVORITE + " = '0';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
}