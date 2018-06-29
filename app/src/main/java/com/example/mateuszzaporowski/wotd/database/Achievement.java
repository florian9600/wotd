package com.example.mateuszzaporowski.wotd.database;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

/**
 * Created by mateuszzaporowski on 05.06.18.
 */

public class Achievement {
    private int id;
    private String image;
    private String name;
    private String description;
    private LocalDate date;

    public Achievement (int id, String image, String name, String description, LocalDate date) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.date = date;
        if (getDateLong() == 0) this.date = null;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getDateLong() {
        return  date != null ? date.toLocalDateTime(org.joda.time.LocalTime.MIDNIGHT)
                .toDateTime(DateTimeZone.UTC).getMillis() : 0;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
