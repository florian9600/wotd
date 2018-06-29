package com.example.mateuszzaporowski.wotd.database;

import com.example.mateuszzaporowski.wotd.support.Transition;

/**
 * Created by mateuszzaporowski on 09.06.18.
 */

public class Translation {
    private int id;
    private String english;
    private String german;
    private String polish;

    public Translation (String english, String german, String polish) {
        this.english = english;
        this.german = german;
        this.polish = polish;
    }

    public String get(String lang) {
        switch (lang.toUpperCase()) {
            case "EN":
                return english;
            case "DE":
                return german;
            case "PL":
                return polish;
        }
        return null;
    }
}
