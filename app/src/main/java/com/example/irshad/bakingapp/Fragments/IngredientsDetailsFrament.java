package com.example.irshad.bakingapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.irshad.bakingapp.Adapters.RecipeIngredientsAdapter;
import com.example.irshad.bakingapp.Model.Recipe;
import com.example.irshad.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsDetailsFrament extends Fragment {
    public static final String TAG = IngredientsDetailsFrament.class.getSimpleName();
    public static final String PARCELABLE_EXTRA_INGREDIENT_DETAIL = "ingredient_detail";

    private ArrayList<Recipe.Ingredient> mIngredientsArrayList;
    private RecipeIngredientsAdapter mRecipeIngredientsAdapter;



    @BindView(R.id.recyclerview_recipe_ingredients_list)
    RecyclerView mRecyclerViewIngredientsList;

    public IngredientsDetailsFrament(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients_details,container,false);
        ButterKnife.bind(this,rootView);

        mRecipeIngredientsAdapter = new RecipeIngredientsAdapter(getContext(),mIngredientsArrayList);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        mRecyclerViewIngredientsList.setLayoutManager(layoutManager);

        mRecyclerViewIngredientsList.setAdapter(mRecipeIngredientsAdapter);

        Log.d(TAG,"Ingredients Fragment Created");

        return rootView;
    }

    public void setIngredientsArrayList(ArrayList<Recipe.Ingredient> ingredients){
        mIngredientsArrayList = ingredients;
        if(mIngredientsArrayList != null && mRecipeIngredientsAdapter != null){
            mRecipeIngredientsAdapter.swapData(mIngredientsArrayList);
            Log.d(TAG,"Ingredients received " + mIngredientsArrayList.size());
        }

        Log.d(TAG,"Empty Ingredients list received !!!!!!!!");

        if(mIngredientsArrayList == null){
            Log.d(TAG,"mIngredientsArrayList is null!!!!");
        }

        if(mRecipeIngredientsAdapter == null){
            Log.d(TAG,"mRecipeIngredientsAdapter is null!!!!!");
        }
    }
}
