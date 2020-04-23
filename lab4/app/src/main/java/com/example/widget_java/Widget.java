package com.example.widget_java;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetConfigureActivity WidgetConfigureActivity}
 */
public class Widget extends AppWidgetProvider {
    private static final String TAG = "myLogs";
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    private static String NOTIFICATION_CHANEL_ID = "Date";

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        CharSequence widgetText = WidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        String selectedDate[] = (widgetText.toString()).split(" ");
        Long sDate = (new Date(Integer.parseInt(selectedDate[0]) - 1900, Integer.parseInt(selectedDate[1]), Integer.parseInt(selectedDate[2]))).getTime();
        widgetText = "Days left:\n" + TimeUnit.MILLISECONDS.toDays(dateDiff(sDate));
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent active = new Intent(context, Widget.class);
        active.setAction(ACTION_WIDGET_RECEIVER);
        Intent config = new Intent(context, WidgetConfigureActivity.class);
        config.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        config.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pIntent = PendingIntent.getActivity(context, appWidgetId,
                config, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        final Long to = dateDiff(sDate) + 32400000;
        new CountDownTimer(to, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onFinish() {
                views.setTextViewText(R.id.appwidget_text,"Date not set");
                Intent intent = new Intent(context, WidgetConfigureActivity.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANEL_ID)
                        .setSmallIcon(R.drawable.catt)
                        .setContentTitle("Date reached")
                        .setContentText("Today is an important day")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_SOUND);
                NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
                notificationManager.notify(appWidgetId, builder.build());
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }.start();
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }


    private static Long dateDiff(Long selectedDate) {
        Date now = new Date();
        Date currentDate = new Date(now.getYear(), now.getMonth(), now.getDate());
        return (selectedDate - currentDate.getTime());
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;}
        NotificationManager notificationManager =
                getSystemService(context, NotificationManager.class);
            NotificationChannel chanel = new NotificationChannel(
                    NOTIFICATION_CHANEL_ID,
                    "Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            chanel.setDescription("description");
            notificationManager.createNotificationChannel(chanel);

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

