package com.example.irshad.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.irshad.bakingapp.Fragments.MasterListFragment;
import com.example.irshad.bakingapp.Model.Recipe;

import java.util.ArrayList;

import static com.example.irshad.bakingapp.MainActivity.PARCEL_KEY_RECIPE;

public class AppWidgetConfigurationActivity extends AppCompatActivity implements MasterListFragment.OnRecipeClickListener{

    public static final String SHARED_PREFS = "com.example.irshad.bakingapp.sharedpreferences";
    public static final String KEY_RECIPE_NAME = "com.example.irshad.bakingapp.keys.recipename";
    public static final String KEY_RECIPE_ID = "com.example.irshad.bakingapp.keys.recipeid";
    public static final String KEY_RECIPE_INGREDIENTS = "com.example.irshad.bakingapp.keys.recipeingredients";

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_widget_configuration);

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if(extras != null){
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if(appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {

        ArrayList<Recipe.Ingredient> ingredientsArrayList = recipe.getIngredients();
        String ingredientsString = "";
        for(int i=0;i<ingredientsArrayList.size();i++){
            ingredientsString =  ingredientsString + (i+1) + ") " + ingredientsArrayList.get(i).getIngredientName() + "  "
                    + ingredientsArrayList.get(i).getQuantity() + " " + ingredientsArrayList.get(i).getMeasurementUnit() + "\n";
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREFS + appWidgetId,MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_RECIPE_ID,recipe.getId());
        editor.putString(KEY_RECIPE_NAME,recipe.getName());
        editor.putString(KEY_RECIPE_INGREDIENTS,ingredientsString);
        editor.apply();

        Log.d("Temporary","Recipe clicked " + recipe.getName() + " INGREDIENTS: " + ingredientsString);
        confirmConfiguration(recipe, ingredientsString);
    }

    public void confirmConfiguration(Recipe recipe, String ingredientsString){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(MainActivity.PARCEL_KEY_RECIPE, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.textview_appwidget_heading, recipe.getName());
        views.setTextViewText(R.id.textview_appwidget_ingredients, ingredientsString);
        views.setOnClickPendingIntent(R.id.textview_appwidget_ingredients, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
