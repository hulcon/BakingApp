package com.example.irshad.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.irshad.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.irshad.bakingapp.Model.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String TAG = RecipeDetailsActivity.class.getSimpleName();
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipe = getIntent().getParcelableExtra(MainActivity.PARCEL_KEY_RECIPE);
        Log.d(TAG,"Got recipe: " + recipe.getName());
        Log.d(TAG,"Ingredients: " + recipe.getIngredients().size());
        Log.d(TAG,"Steps: " +  recipe.getSteps().size());
        Log.d(TAG, "Ingredients: " + recipe.getIngredients());

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(recipe.getName());
        }

        RecipeDetailsFragment recipeDetailsFragment = (RecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_details_fragment);
        recipeDetailsFragment.setRecipe(recipe);

    }
}
