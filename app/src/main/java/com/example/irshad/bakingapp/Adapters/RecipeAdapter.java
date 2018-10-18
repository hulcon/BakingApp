package com.example.irshad.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.irshad.bakingapp.Model.Recipe;
import com.example.irshad.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>  {

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private ArrayList<Recipe> mRecipeArrayList;
    private RecipeAdapterClickHandler mClickHandler;

    public interface RecipeAdapterClickHandler {
        void onClick(Recipe recipe);
    }



    public RecipeAdapter(Context context, RecipeAdapterClickHandler clickHandler, ArrayList<Recipe> recipes){
        mInflater = LayoutInflater.from(context);
        mRecipeArrayList = recipes;
        mClickHandler = clickHandler;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = mInflater.inflate(R.layout.recipe_recycler_view_item,viewGroup,false);
        mItemView.setFocusable(true);
        return new RecipeViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {
        recipeViewHolder.recipeName.setText(mRecipeArrayList.get(i).getName());
        if(!TextUtils.isEmpty(mRecipeArrayList.get(i).getImage())) {
            Picasso.get().load(mRecipeArrayList.get(i).getImage()).placeholder(R.drawable.recipe).into(recipeViewHolder.recipeItemBackground);
        }else {
            recipeViewHolder.recipeItemBackground.setImageResource(R.drawable.recipe);
        }

        String servingsText = (int) mRecipeArrayList.get(i).getServings() + " Servings";
        recipeViewHolder.textViewServings.setText(servingsText);
    }

    @Override
    public int getItemCount() {
        if(mRecipeArrayList == null){
            return 0;
        }
        return mRecipeArrayList.size();
    }

    public void swapDataList(ArrayList<Recipe> recipes){
        mRecipeArrayList = recipes;
        Log.d(TAG,"received recipe list with items: " + mRecipeArrayList.size());
        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.textview_recipe_name)
        TextView recipeName;

        @BindView(R.id.imageView_recipe_item_background)
        ImageView recipeItemBackground;

        @BindView(R.id.textview_recipe_servings)
        TextView textViewServings;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mRecipeArrayList.get(adapterPosition));
        }
    }
}
