package com.example.irshad.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.irshad.bakingapp.Fragments.IngredientsDetailsFrament;
import com.example.irshad.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.irshad.bakingapp.Fragments.StepDetailsFragment;
import com.example.irshad.bakingapp.Model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.OnIngredientsClickListener,
        RecipeDetailsFragment.OnStepsClickListener, StepDetailsFragment.WasVideoPlayingCallback{

    public static final String TAG = RecipeDetailsActivity.class.getSimpleName();

    private Recipe recipe;
    private ArrayList<Recipe.Step> mRecipeStepsArrayList;
    private boolean tabletLandscapeMode;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipe = getIntent().getParcelableExtra(MainActivity.PARCEL_KEY_RECIPE);
        Log.d(TAG,"Got recipe: " + recipe.getName());
        Log.d(TAG,"Ingredients: " + recipe.getIngredients().size());
        Log.d(TAG,"Steps: " +  recipe.getSteps().size());
        Log.d(TAG, "Ingredients: " + recipe.getIngredients());

        if(recipe != null){
            mRecipeStepsArrayList = recipe.getSteps();
        }

        if(findViewById(R.id.tablet_landscape_mode) != null){
            tabletLandscapeMode = true;

            IngredientsDetailsFrament ingredientsDetailsFrament = new IngredientsDetailsFrament();
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();

            //stepDetailsFragment.setStep(recipe.getSteps().get(0));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.ingredients_details_container,ingredientsDetailsFrament)
                    .add(R.id.step_details_container,stepDetailsFragment)
                    .commit();
            ingredientsDetailsFrament.setIngredientsArrayList(recipe.getIngredients());

        } else {
            tabletLandscapeMode = false;
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(recipe.getName());
        }

        RecipeDetailsFragment recipeDetailsFragment = (RecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_details_fragment);
        if (recipeDetailsFragment != null) {
            recipeDetailsFragment.setRecipe(recipe);
        }

    }

    @Override
    public void onIngredientsClicked(Recipe recipe) {
        if(tabletLandscapeMode){
            replaceIngredientsFragment(recipe);

        } else {
            Intent intent = new Intent(getApplicationContext(), IngredientsDetailsActivity.class);
            intent.putExtra(MainActivity.PARCEL_KEY_RECIPE,recipe);
            startActivity(intent);
        }
    }

    @Override
    public void onStepsClicked(ArrayList<Recipe.Step> recipeStepsArrayList, int currentStepClicked) {
        Toast.makeText(this, "Step: " + recipeStepsArrayList.get(currentStepClicked).getShortDescription(), Toast.LENGTH_SHORT).show();
        if(tabletLandscapeMode){
            replaceStepsFragment(recipeStepsArrayList,currentStepClicked);
        } else {
            Intent intent = new Intent(getApplicationContext(), StepDetailsActivity.class);
            intent.putExtra(StepDetailsFragment.PARCELABLE_EXTRA_STEP_ARRAY_LIST,recipeStepsArrayList);
            intent.putExtra(StepDetailsFragment.CURRENT_STEP_INDEX,currentStepClicked);
            startActivity(intent);
        }
    }

    private void replaceIngredientsFragment(Recipe recipe){

        FrameLayout stepDetailsContainerFrameLayout = findViewById(R.id.step_details_container);
        FrameLayout ingredientsDetailsContainerFrameLayout = findViewById(R.id.ingredients_details_container);

        ingredientsDetailsContainerFrameLayout.setVisibility(View.VISIBLE);
        stepDetailsContainerFrameLayout.setVisibility(View.GONE);
        IngredientsDetailsFrament ingredientsDetailsFrament = new IngredientsDetailsFrament();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.ingredients_details_container,ingredientsDetailsFrament)
                .commit();
        ingredientsDetailsFrament.setIngredientsArrayList(recipe.getIngredients());
    }

    private void replaceStepsFragment(ArrayList<Recipe.Step> recipeSteps,int currentStepIndex){
        FrameLayout stepDetailsContainerFrameLayout = findViewById(R.id.step_details_container);
        FrameLayout ingredientsDetailsContainerFrameLayout = findViewById(R.id.ingredients_details_container);

        ingredientsDetailsContainerFrameLayout.setVisibility(View.GONE);
        stepDetailsContainerFrameLayout.setVisibility(View.VISIBLE);
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setStepsArrayList(recipeSteps,currentStepIndex);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_details_container,stepDetailsFragment)
                .commit();
    }

    @Override
    public void onDeviceRotatedWhileVideoPlaying(int currentStepIndex, boolean videoPlayingWhenDeviceRotated, long videoCurrentPosition, boolean playWhenReadyStatus) {

        if(!tabletLandscapeMode){
            if(videoPlayingWhenDeviceRotated){
                Intent intent = new Intent(getApplicationContext(), StepDetailsActivity.class);
                intent.putExtra(StepDetailsFragment.PARCELABLE_EXTRA_STEP_ARRAY_LIST, mRecipeStepsArrayList);
                intent.putExtra(StepDetailsFragment.CURRENT_STEP_INDEX, currentStepIndex);
                intent.putExtra(StepDetailsFragment.EXTRA_KEY_WAS_VIDEO_PLAYING, videoPlayingWhenDeviceRotated);
                intent.putExtra(StepDetailsFragment.EXTRA_KEY_PLAYER_CURRENT_POSITION, videoCurrentPosition);
                intent.putExtra(StepDetailsFragment.EXTRA_KEY_PLAYER_WHEN_READY, playWhenReadyStatus);
                startActivity(intent);
            }
        }
    }
}
