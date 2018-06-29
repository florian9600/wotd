package com.example.mateuszzaporowski.wotd.tabs.social;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mateuszzaporowski.wotd.R;
import com.example.mateuszzaporowski.wotd.database.Achievement;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by mateuszzaporowski on 04.06.18.
 */

public class AchievementsRowAdapter extends ArrayAdapter<Achievement> {
    private Activity context;
    private Achievement[] achievements;

    public AchievementsRowAdapter(Activity context, Achievement[] achievements) {
        super(context, R.layout.list_row_achievements, achievements);
        this.context = context;
        this.achievements = achievements;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_row_achievements, null, true);

        ImageView imageView = rowView.findViewById(R.id.achievement_image);
        imageView.setImageResource(context.getResources().getIdentifier(achievements[position].getImage(), "drawable", context.getPackageName()));

        TextView nameView = rowView.findViewById(R.id.achievement_name);
        nameView.setText(context.getResources().getIdentifier(achievements[position].getName(), "string", context.getPackageName()));

        TextView descriptionView = rowView.findViewById(R.id.achievement_description);
        descriptionView.setText(context.getResources().getIdentifier(achievements[position].getDescription(), "string", context.getPackageName()));

        TextView dateView = rowView.findViewById(R.id.achievement_date);
        LocalDate date = achievements[position].getDate();
        if (date != null) {
            dateView.setText(date.toString(DateTimeFormat.forPattern("d.M.Y")));
        } else {
            dateView.setText(R.string.ach_not_unlocked);
        }

        return rowView;
    }
}
