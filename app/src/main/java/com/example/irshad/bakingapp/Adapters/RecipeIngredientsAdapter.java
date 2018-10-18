package com.example.irshad.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.irshad.bakingapp.Model.Recipe;
import com.example.irshad.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.RecipeIngredientsViewHolder> {

    public static final String TAG = RecipeIngredientsAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    ArrayList<Recipe.Ingredient> mRecipeIngredientsArrayList;

    public RecipeIngredientsAdapter(Context context, ArrayList<Recipe.Ingredient> ingredientArrayList){
        mLayoutInflater = LayoutInflater.from(context);
        mRecipeIngredientsArrayList = ingredientArrayList;
    }
    @NonNull
    @Override
    public RecipeIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = mLayoutInflater.inflate(R.layout.recyclerview_recipe_ingredients_details_item, viewGroup, false);
        return new RecipeIngredientsViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientsViewHolder recipeIngredientsViewHolder, int i) {

        recipeIngredientsViewHolder.textViewIngredientName.setText(mRecipeIngredientsArrayList.get(i).getIngredientName());

        String quantityText = mRecipeIngredientsArrayList.get(i).getQuantity() + " " + mRecipeIngredientsArrayList.get(i).getMeasurementUnit();
        recipeIngredientsViewHolder.textViewIngredientQuantity.setText(quantityText);
    }

    @Override
    public int getItemCount() {
        if(mRecipeIngredientsArrayList == null){
            return 0;
        }
        return mRecipeIngredientsArrayList.size();
    }

    public void swapData(ArrayList<Recipe.Ingredient> ingredients){
        mRecipeIngredientsArrayList = ingredients;
        notifyDataSetChanged();
    }

    public class RecipeIngredientsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textview_ingredient_name)
        TextView textViewIngredientName;

        @BindView(R.id.textview_ingredient_quantity)
        TextView textViewIngredientQuantity;

        public RecipeIngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
