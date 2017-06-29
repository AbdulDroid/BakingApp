package com.example.floating.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.data.Recipe;
import com.example.floating.bakingapp.database.RecipeDbHelper;
import com.example.floating.bakingapp.ui.RecipeActivity;


/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RecipeDbHelper provider = new RecipeDbHelper(context);

        Recipe recipe = provider.getViewedRecipe();

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            String recipeName;
            String recipeIngredient;

            if (recipe != null){
                recipeName = recipe.getName();
                recipeIngredient = recipe.getIngredient_string();
            }else {
                recipeName = "No Recipe Selected";
                recipeIngredient = "No Ingredients to Display";
            }

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            views.setTextViewText(R.id.appwidget_header, recipeName);
            views.setTextViewText(R.id.widget_ingredient_list, recipeIngredient);

            Intent recipeIntent = new Intent(context, RecipeActivity.class);
            recipeIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            recipeIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

/*            PendingIntent recipePendingIntent = PendingIntent.getBroadcast(context,
                    0, recipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

