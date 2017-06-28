package com.example.floating.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.floating.bakingapp.R;


/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            CharSequence widgetText = "Recipe Name";

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            views.setTextViewText(R.id.appwidget_header, widgetText);
            views.setTextViewText(R.id.widget_ingredient_list, "List of Ingredient " +
                    "used in making Recipe Name \n " +
                    "1. Recipe 1 \n 2. Recipe 2 \n 3. Recipe 3");

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

