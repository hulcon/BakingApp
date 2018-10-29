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

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    public static final String TAG = RecipeStepsAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private ArrayList<Recipe.Step> mRecipeStepsArrayList;
    private RecipeStepsAdapterClickHandler mClickHandler;

    public interface RecipeStepsAdapterClickHandler{
        void onClick(ArrayList<Recipe.Step> recipeStepsArrayList,int currentStepClicked);
    }

    public RecipeStepsAdapter(Context context, RecipeStepsAdapterClickHandler clickHandler, ArrayList<Recipe.Step> recipeStepsArrayList){
        mLayoutInflater = LayoutInflater.from(context);
        mClickHandler = clickHandler;
        mRecipeStepsArrayList = recipeStepsArrayList;
    }

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = mLayoutInflater.inflate(R.layout.recycler_view_recipe_steps_item,viewGroup,false);
        mItemView.setFocusable(true);
        return new RecipeStepsViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder recipeStepsViewHolder, int i) {
        recipeStepsViewHolder.textViewStepName.setText(mRecipeStepsArrayList.get(i).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if(mRecipeStepsArrayList == null){
            return 0;
        }
        return mRecipeStepsArrayList.size();
    }

    public void swapData(ArrayList<Recipe.Step> steps){
        mRecipeStepsArrayList = steps;
        notifyDataSetChanged();
    }

    public class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textview_recipe_steps_step_name)
        TextView textViewStepName;

        public RecipeStepsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mRecipeStepsArrayList,adapterPosition);
        }
    }
}
