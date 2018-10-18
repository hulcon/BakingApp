package com.example.irshad.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.irshad.bakingapp.Fragments.IngredientsDetailsFrament;
import com.example.irshad.bakingapp.Model.Recipe;

public class IngredientsDetailsActivity extends AppCompatActivity {

    public static final String TAG = IngredientsDetailsActivity.class.getSimpleName();
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_details);

        recipe = getIntent().getParcelableExtra(MainActivity.PARCEL_KEY_RECIPE);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(recipe.getName() + " Ingredients");
        }

        IngredientsDetailsFrament ingredientsDetailsFrament = (IngredientsDetailsFrament) getSupportFragmentManager().findFragmentById(R.id.ingredient_details_Fragment);
        ingredientsDetailsFrament.setIngredientsArrayList(recipe.getIngredients());
    }
}
