package com.example.irshad.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import static android.content.Context.MODE_PRIVATE;
import static com.example.irshad.bakingapp.AppWidgetConfigurationActivity.KEY_RECIPE_INGREDIENTS;
import static com.example.irshad.bakingapp.AppWidgetConfigurationActivity.KEY_RECIPE_NAME;
import static com.example.irshad.bakingapp.AppWidgetConfigurationActivity.SHARED_PREFS;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS + appWidgetId,MODE_PRIVATE);
        String recipeName = pref.getString(KEY_RECIPE_NAME,"");
        String recipeIngredients = pref.getString(KEY_RECIPE_INGREDIENTS,"");


        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.textview_appwidget_heading, recipeName);
        views.setTextViewText(R.id.textview_appwidget_ingredients, recipeIngredients);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

