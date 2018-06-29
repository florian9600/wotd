package com.example.mateuszzaporowski.wotd.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mateuszzaporowski on 05.06.18.
 */

public class AchievementHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "achievementDB.db";

    private static final String TABLE_ACHIEVEMENT = "achievement";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";

    private Resources resources;
    private Context context;

    public AchievementHandler(Context context, SQLiteDatabase.CursorFactory factory, Resources resources) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.resources = resources;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ACHIEVEMENT_TABLE =
                "CREATE TABLE " + TABLE_ACHIEVEMENT + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_IMAGE + " VARCHAR,"
                        + COLUMN_NAME + " VARCHAR,"
                        + COLUMN_DESCRIPTION + " VARCHAR,"
                        + COLUMN_DATE + " INTEGER"
                        + ")";
        sqLiteDatabase.execSQL(CREATE_ACHIEVEMENT_TABLE);

        InputStream ins = resources.openRawResource(
                resources.getIdentifier("achievements",
                        "raw", "com.example.mateuszzaporowski.wotd"));

        String achievementsCSV = null;
        try {
            achievementsCSV = IOUtils.toString(ins, (String) null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line: achievementsCSV.split("\n")) {
            String[] phrases = line.split(",");
            Achievement achievement = new Achievement(
                    -1,
                    phrases[0],
                    phrases[1],
                    phrases[2],
                    null
            );
            addAchievement(achievement, sqLiteDatabase);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENT);
        onCreate(sqLiteDatabase);
    }

    private void addAchievement(Achievement achievement, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, achievement.getImage());
        values.put(COLUMN_NAME, achievement.getName());
        values.put(COLUMN_DESCRIPTION, achievement.getDescription());
        values.put(COLUMN_DATE, achievement.getDateLong());

        db.insert(TABLE_ACHIEVEMENT, null, values);
    }

    public ArrayList<Achievement> getAchievements() {
        String query = "Select * FROM " + TABLE_ACHIEVEMENT;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Achievement> articles = new ArrayList<Achievement>();

        cursor.moveToFirst();
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            do {
                articles.add(new Achievement(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    new LocalDate(Long.parseLong(cursor.getString(4)))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return articles;
    }

    private void unlockAchievement(String name) {
        String query = "UPDATE " + TABLE_ACHIEVEMENT + " SET "
                + COLUMN_DATE + " = '" +
                new LocalDate()
                .toLocalDateTime(org.joda.time.LocalTime.MIDNIGHT)
                .toDateTime(DateTimeZone.UTC).getMillis()
                + "' WHERE " + COLUMN_NAME + " = '" + name + "' AND " +
                COLUMN_DATE + " = 0;";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    public void checkForNewAchievements(String lang) {
        ArticleHandler articleHandler = new ArticleHandler(context, null);
        ArrayList<Article> articles = articleHandler.findArticles(lang, null);

        int streak = 0;
        for (Article article: articles) {
            if (article.getRead() == 1) {
                unlockAchievement("ach_first_article");
                streak++;
            } else {
                streak = 0;
            }

            if (article.getFavorite() == 1) {
                unlockAchievement("ach_favorite");
            }

            switch (streak) {
                case 7:
                    unlockAchievement("ach_7");
                    break;
                case 14:
                    unlockAchievement("ach_14");
                    break;
                case 30:
                    unlockAchievement("ach_30");
                    break;
            }
        }
    }

    public void updateAllAchievementsToUndone() {
        String query = "UPDATE " + TABLE_ACHIEVEMENT + " SET " + COLUMN_DATE + " = '0';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
}