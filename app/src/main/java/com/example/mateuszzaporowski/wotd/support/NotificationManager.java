package com.example.mateuszzaporowski.wotd.support;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.mateuszzaporowski.wotd.R;
import com.example.mateuszzaporowski.wotd.database.Article;
import com.example.mateuszzaporowski.wotd.database.ArticleHandler;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mateuszzaporowski on 07.06.18.
 */

public class NotificationManager extends Service {
    private int daysPassedSinceLastSolvedArticleUntilInitialization;
    private int setting;
    private long dateOfInitialization;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        daysPassedSinceLastSolvedArticleUntilInitialization = (int) bundle.getSerializable("daysPassed");
        setting = (int) bundle.getSerializable("setting");
        dateOfInitialization = (long) bundle.getSerializable("date");
        LocalDate dayOfInitialization = new LocalDate(dateOfInitialization);

        if (setting == 0) {
            return super.onStartCommand(intent, flags, startId);
        }

        int daysPassedSinceInitialization = 0;
        LocalDate temp = new LocalDate();

        while (!temp.isEqual(dayOfInitialization)) {
            temp = temp.minusDays(1);
            daysPassedSinceInitialization++;
        }

        if (daysPassedSinceLastSolvedArticleUntilInitialization + daysPassedSinceInitialization < setting) {
            return super.onStartCommand(intent, flags, startId);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(R.drawable.ic_home)
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(getResources().getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void setUpNotification(Context context) {
        Settings settings = new Settings(context);

        ArticleHandler handler = new ArticleHandler(context, null);
        handler.findArticles(settings.languagesValue[settings.getLanguages()], null);

        ArrayList<Article> articles = handler.findArticles(settings.languagesValue[settings.getLanguages()], null);
        int daysPassedSinceLastSolvedArticleUntilInitialization = 0;
        while (articles.get(articles.size() - 1 - daysPassedSinceLastSolvedArticleUntilInitialization).getRead() == 0) {
            daysPassedSinceLastSolvedArticleUntilInitialization++;
            if (daysPassedSinceLastSolvedArticleUntilInitialization > settings.notificationsValue[settings.getNotifications()]) break;
        }

        Intent myIntent = new Intent(context, NotificationManager.class);
        myIntent.putExtra("setting", settings.notificationsValue[settings.getNotifications()]);
        myIntent.putExtra("daysPassed", daysPassedSinceLastSolvedArticleUntilInitialization);
        myIntent.putExtra("date", new LocalDate().toLocalDateTime(org.joda.time.LocalTime.MIDNIGHT)
                .toDateTime(DateTimeZone.UTC).getMillis());
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 56);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours
    }
}