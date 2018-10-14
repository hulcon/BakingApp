package com.example.irshad.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.irshad.bakingapp.Adapters.RecipeAdapter;
import com.example.irshad.bakingapp.Fragments.MasterListFragment;
import com.example.irshad.bakingapp.Model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements MasterListFragment.OnRecipeClickListener{

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String PARCEL_KEY_RECIPE = "recipeParcelKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }



    @Override
    public void onRecipeClicked(Recipe recipe) {
        Log.d(TAG,"Item clicked " + recipe.getName());
        Intent intent = new Intent(getApplicationContext(),RecipeDetailsActivity.class);
        intent.putExtra(PARCEL_KEY_RECIPE,recipe);
        startActivity(intent);
    }
}
