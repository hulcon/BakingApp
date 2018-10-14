package com.example.irshad.bakingapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.irshad.bakingapp.Adapters.RecipeStepsAdapter;
import com.example.irshad.bakingapp.Model.Recipe;
import com.example.irshad.bakingapp.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This fragment is responsible for displaying the recipe details.
 * This fragment is called into action when a recipe is clicked
 * in the master recipe list.
 */
public class RecipeDetailsFragment extends Fragment implements RecipeStepsAdapter.RecipeStepsAdapterClickHandler {

    public static final String TAG = RecipeDetailsFragment.class.getSimpleName();

    private Recipe mRecipe;
    private ArrayList<Recipe.Step> mRecipeStepsArrayList;
    private RecipeStepsAdapter mRecipeStepsAdapter;

    @BindView(R.id.recyclerview_recipe_steps)
    RecyclerView recyclerviewRecipeSteps;




    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_details,container,false);
        ButterKnife.bind(this,rootView);

        mRecipeStepsAdapter = new RecipeStepsAdapter(getContext(),this,mRecipeStepsArrayList);
        recyclerviewRecipeSteps.setAdapter(mRecipeStepsAdapter);
        recyclerviewRecipeSteps.setLayoutManager(new GridLayoutManager(getContext(),1));

        Log.d(TAG,"Fragment created");

        return rootView;
    }



    public void setRecipe(Recipe recipe){
        mRecipe = recipe;
        if(mRecipe != null){
            mRecipeStepsArrayList = recipe.getSteps();
            mRecipeStepsAdapter.swapData(mRecipeStepsArrayList);
        }
        Log.d(TAG,"Recipe received with name: " + recipe.getName());
    }

    @Override
    public void onClick(Recipe.Step recipeStep) {

    }
}
