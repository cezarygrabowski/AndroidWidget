package com.example.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;
import java.util.Random;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class WidgetProvider extends AppWidgetProvider {
    public static final String CHANGE_IMAGE_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String OPEN_BROWSER_ACTION = "com.example.android.stackwidget.OPEN_BROWSER_ACTION";
    public static final String ACTION_WIDGET_PLAY = "com.example.android.stackwidget.PlaySong";
    public static final String ACTION_WIDGET_PAUSE = "com.example.android.stackwidget.PauseSong";
    public static final String ACTION_WIDGET_STOP = "com.example.android.stackwidget.StopSong";
    public static final String ACTION_WIDGET_NEXT = "com.example.android.stackwidget.NextSong";

    private final int INTENT_FLAGS = 0;
    private final int REQUEST_CODE = 0;

    private int[] images = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(CHANGE_IMAGE_ACTION)) {
            Toast.makeText(context, "image changed ", Toast.LENGTH_SHORT).show();
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
            views.setImageViewResource(R.id.image_view, getRandom(images));
            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), WidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int appWidgetId : appWidgetIds) {
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }

        if (intent.getAction().equals(OPEN_BROWSER_ACTION)) {
            Toast.makeText(context, "button clicked", Toast.LENGTH_SHORT).show();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            browserIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }

        if (intent.getAction().equals(WidgetProvider.ACTION_WIDGET_PLAY)) {
            Toast.makeText(context, "play ", Toast.LENGTH_SHORT).show();
        }
        if (intent.getAction().equals(WidgetProvider.ACTION_WIDGET_PAUSE)) {
            Toast.makeText(context, "pause ", Toast.LENGTH_SHORT).show();
        }
        if (intent.getAction().equals(WidgetProvider.ACTION_WIDGET_STOP)) {
            Toast.makeText(context, "stop ", Toast.LENGTH_SHORT).show();
        }
        if (intent.getAction().equals(WidgetProvider.ACTION_WIDGET_NEXT)) {
            Toast.makeText(context, "next ", Toast.LENGTH_SHORT).show();
        }

        super.onReceive(context, intent);
    }

    private int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update each of the widgets with the remote adapter
        for (int appWidgetId : appWidgetIds) {

            Intent imageIntent = new Intent(context, WidgetProvider.class);
            imageIntent.setAction(CHANGE_IMAGE_ACTION);
            PendingIntent pending = PendingIntent.getBroadcast(context, 0, imageIntent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
            views.setOnClickPendingIntent(R.id.image_view, pending);

            Intent browserIntent = new Intent(context, WidgetProvider.class);
            browserIntent.setAction(OPEN_BROWSER_ACTION);
            PendingIntent pendingBrowserIntent = PendingIntent.getBroadcast(context, 0, browserIntent, 0);
            views.setOnClickPendingIntent(R.id.button3, pendingBrowserIntent);

            handleControlButtons(context, views);

            Intent intent = new Intent(context, StackWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(appWidgetId, R.id.stack_view, intent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void handleControlButtons(Context context, RemoteViews views) {
        Intent playIntent = new Intent(context, WidgetProvider.class);
        playIntent.setAction(ACTION_WIDGET_PLAY);
        PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(context, 0, playIntent, 0);
        views.setOnClickPendingIntent(R.id.play, pendingPlayIntent);

        Intent pauseIntent = new Intent(context, WidgetProvider.class);
        pauseIntent.setAction(ACTION_WIDGET_PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(context, 0, pauseIntent, 0);
        views.setOnClickPendingIntent(R.id.pause, pendingPauseIntent);

        Intent stopIntent = new Intent(context, WidgetProvider.class);
        stopIntent.setAction(ACTION_WIDGET_STOP);
        PendingIntent pendingStopIntent = PendingIntent.getBroadcast(context, 0, stopIntent, 0);
        views.setOnClickPendingIntent(R.id.stop, pendingStopIntent);

        Intent nextIntent = new Intent(context, WidgetProvider.class);
        nextIntent.setAction(ACTION_WIDGET_NEXT);
        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(context, 0, nextIntent, 0);
        views.setOnClickPendingIntent(R.id.next, pendingNextIntent);

    }
}