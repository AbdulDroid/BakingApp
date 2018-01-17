package com.example.floating.bakingapp.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import com.example.floating.bakingapp.R
import com.example.floating.bakingapp.database.RecipeDbHelper
import com.example.floating.bakingapp.recipes.RecipeActivity


/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        val provider = RecipeDbHelper(context)

        val recipe = provider.viewedRecipe

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {

            val recipeName: String?
            val recipeIngredient: String?

            if (recipe != null) {
                recipeName = recipe.name
                recipeIngredient = recipe.ingredient_string
            } else {
                recipeName = "No Recipe Selected"
                recipeIngredient = "No Ingredients to Display"
            }

            val views = RemoteViews(context.packageName, R.layout.app_widget)
            views.setTextViewText(R.id.appwidget_header, recipeName)
            views.setTextViewText(R.id.widget_ingredient_list, recipeIngredient)

            val recipeIntent = Intent(context, RecipeActivity::class.java)
            recipeIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            recipeIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)

            /*            PendingIntent recipePendingIntent = PendingIntent.getBroadcast(context,
                    0, recipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

