package com.example.mateuszzaporowski.wotd.database;

import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Created by mateuszzaporowski on 08.05.18.
 */

public class Article {
    private int id;
    private LocalDate date;
    private long dateLong;
    private String word;
    private String content;
    private String pronunciation;
    private String language;
    private int read;
    private int favorite;
    private int translation;

    public Article(int id, long date, String word, String content, String pronunciation, String language, int read, int favorite, int translation) {
        this.id = id;
        this.dateLong = date;
        this.date = new LocalDate(date);
        this.word = word;
        this.content = content;
        this.pronunciation = pronunciation;
        this.language = language;
        this.read = read;
        this.favorite = favorite;
        this.translation = translation;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getDateLong() {
        return dateLong;
    }

    public String getWord() {
        return word;
    }

    public String getContent() {
        return content;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getLanguage() {
        return language;
    }

    public int getRead() {
        return read;
    }

    public int getFavorite() {
        return favorite;
    }

    public void swapFavorite() {
        favorite = (favorite + 1) % 2;
    }

    public void setReadTrue() { read = 1; }

    public int getTranslation() {
        return translation;
    }
}