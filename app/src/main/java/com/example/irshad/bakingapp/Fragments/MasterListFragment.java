package com.example.irshad.bakingapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.irshad.bakingapp.Adapters.RecipeAdapter;
import com.example.irshad.bakingapp.Model.Recipe;
import com.example.irshad.bakingapp.R;
import com.example.irshad.bakingapp.Utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListFragment extends Fragment implements RecipeAdapter.RecipeAdapterClickHandler{

    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    private static final String TAG = MasterListFragment.class.getSimpleName();
    public static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static final String SAVED_INSTANCE_KEY_CACHED_SERVER_RESPONSE = "cachedServerResponse";
    private static final int DISPLAY_DATA_UI = 10;
    private static final int DISPLAY_LOADING = 20;
    private static final int DISPLAY_ERROR = 30;
    private String cachedServerJsonResponse;


    @BindView(R.id.recycler_view_main_activity)
    RecyclerView recyclerViewRecipes;

    @BindView(R.id.progressbar_main_activity)
    ProgressBar progressBar;

    @BindView(R.id.textview_loading_main_activity)
    TextView textViewLoadingMessage;

    OnRecipeClickListener mCallback;

    public interface OnRecipeClickListener{
        void onRecipeClicked(Recipe recipe);
    }

    public MasterListFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (OnRecipeClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnRecipeClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_list,container,false);
        ButterKnife.bind(this,rootView);

        mRecipeAdapter = new RecipeAdapter(getContext(),this,recipeArrayList);
        recyclerViewRecipes.setAdapter(mRecipeAdapter);

        final int recyclerviewColumns = getResources().getInteger(R.integer.recipe_recycler_view_columns);
        recyclerViewRecipes.setLayoutManager(new GridLayoutManager(getContext(),recyclerviewColumns));

        if(savedInstanceState != null && !TextUtils.isEmpty(savedInstanceState.getString(SAVED_INSTANCE_KEY_CACHED_SERVER_RESPONSE))){
            cachedServerJsonResponse = savedInstanceState.getString(SAVED_INSTANCE_KEY_CACHED_SERVER_RESPONSE);
            displayUI(DISPLAY_DATA_UI);
            extractDataFromJsonIntoAdapter(cachedServerJsonResponse);
            Log.d(TAG,"Getting data from cache");
        } else {
            if(NetworkUtils.isOnline(getContext())){
                displayUI(DISPLAY_LOADING);
                sendNetworkRequest();
                Log.d(TAG,"Making active network call");
            } else {
                displayUI(DISPLAY_ERROR);
                textViewLoadingMessage.setText(R.string.device_offline_message);
            }
        }


        return rootView;
    }

    public void sendNetworkRequest(){

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, RECIPES_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Response Received for volley request!!");
                        displayUI(DISPLAY_DATA_UI);
                        extractDataFromJsonIntoAdapter(response);
                        Log.d(TAG,"Adapter Size now is: " + mRecipeAdapter.getItemCount());
                        cachedServerJsonResponse = response;
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayUI(DISPLAY_ERROR);
                textViewLoadingMessage.setText(R.string.device_offline_message);
                Log.e(TAG,"Volley request error:" + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }


    private void displayUI(int displayLevel){
        switch (displayLevel){
            case DISPLAY_DATA_UI:
                textViewLoadingMessage.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                recyclerViewRecipes.setVisibility(View.VISIBLE);
                Log.d(TAG,"Showing Recycler view...");
                break;

            case DISPLAY_LOADING:
                textViewLoadingMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                recyclerViewRecipes.setVisibility(View.GONE);
                Log.d(TAG,"Displaying Loading UI...");
                break;

            case DISPLAY_ERROR:
                textViewLoadingMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                recyclerViewRecipes.setVisibility(View.GONE);
                break;

            default:
                throw new IllegalArgumentException("Passed an illegal argument to displayUI: " + displayLevel);
        }

    }

    private void extractDataFromJsonIntoAdapter(String serverResponse){
        if(TextUtils.isEmpty(serverResponse)){
            return;
        }

        mRecipeAdapter.swapDataList(Recipe.createRecipeListFromJson(serverResponse));
    }

    @Override
    public void onClick(Recipe recipe) {
        mCallback.onRecipeClicked(recipe);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_INSTANCE_KEY_CACHED_SERVER_RESPONSE,cachedServerJsonResponse);
    }
}
