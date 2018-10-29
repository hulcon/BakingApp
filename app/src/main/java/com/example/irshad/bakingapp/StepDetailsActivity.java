package com.example.irshad.bakingapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.irshad.bakingapp.Fragments.StepDetailsFragment;
import com.example.irshad.bakingapp.Model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String TAG = StepDetailsActivity.class.getSimpleName();
    private ArrayList<Recipe.Step> recipeStepsArrayList;
    private int currentStepIndex;
    private boolean phonePortraitMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);


        recipeStepsArrayList = getIntent().getParcelableArrayListExtra(StepDetailsFragment.PARCELABLE_EXTRA_STEP_ARRAY_LIST);

        if(savedInstanceState != null){
            currentStepIndex = savedInstanceState.getInt(StepDetailsFragment.CURRENT_STEP_INDEX);
        } else {
            currentStepIndex = getIntent().getIntExtra(StepDetailsFragment.CURRENT_STEP_INDEX, 0);
        }
        /*if(getSupportActionBar() != null){
            if(recipeStepsArrayList != null){
                if(Integer.parseInt(recipeStepsArrayList.get(currentStepIndex).getId())>0){
                    getSupportActionBar().setTitle("Step " + recipeStepsArrayList.get(currentStepIndex).getId());
                }
                else {
                    getSupportActionBar().setTitle("Intro");
                }
            }
        }*/
        setStepsActivityTitle();

        /*StepDetailsFragment stepDetailsFragment = (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_fragment);
        if (stepDetailsFragment != null) {
            stepDetailsFragment.setStepsArrayList(recipeStepsArrayList,currentStepIndex);
            stepDetailsFragment.updateUI();
        }*/



        if(findViewById(R.id.constraintlayout_step_detail_phone_portrait_layout) != null){
            phonePortraitMode = true;
            Log.d(TAG,"************** PORTRAIT MODE ACTIVE ***************** iNDEX IS" + currentStepIndex);

            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_details_fragment_container_for_step_activity,stepDetailsFragment)
                    .commit();
            stepDetailsFragment.setStepsArrayList(recipeStepsArrayList,currentStepIndex);

            FloatingActionButton fabPreviousStep = findViewById(R.id.fab_previous_step);
            FloatingActionButton fabNextStep = findViewById(R.id.fab_next_step);

            fabPreviousStep.setOnClickListener(new FloatingActionButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentStepIndex > 0){
                        currentStepIndex--;
                        StepDetailsFragment newStepFragment = new StepDetailsFragment();
                        newStepFragment.setStepsArrayList(recipeStepsArrayList,currentStepIndex);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.step_details_fragment_container_for_step_activity,newStepFragment)
                                .commit();
                        setStepsActivityTitle();
                        showOrHidePrevNextButtons();
                    }
                }
            });


            fabNextStep.setOnClickListener(new FloatingActionButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentStepIndex < (recipeStepsArrayList.size()-1)){
                        currentStepIndex++;
                        StepDetailsFragment newStepFragment = new StepDetailsFragment();
                        newStepFragment.setStepsArrayList(recipeStepsArrayList,currentStepIndex);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.step_details_fragment_container_for_step_activity,newStepFragment)
                                .commit();
                        setStepsActivityTitle();
                        showOrHidePrevNextButtons();
                    }
                }
            });

            /*Button buttonPreviousStep = findViewById(R.id.button_previous_step);
            Button buttonNextStep = findViewById(R.id.button_next_step);
            showOrHidePrevNextButtons();
            buttonNextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentStepIndex < (recipeStepsArrayList.size()-1)){
                        currentStepIndex++;
                        StepDetailsFragment newStepFragment = new StepDetailsFragment();
                        newStepFragment.setStepsArrayList(recipeStepsArrayList,currentStepIndex);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.step_details_fragment_container_for_step_activity,newStepFragment)
                                .commit();
                        setStepsActivityTitle();
                        showOrHidePrevNextButtons();
                    }
                }
            });


            buttonPreviousStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentStepIndex > 0){
                        currentStepIndex--;
                        StepDetailsFragment newStepFragment = new StepDetailsFragment();
                        newStepFragment.setStepsArrayList(recipeStepsArrayList,currentStepIndex);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.step_details_fragment_container_for_step_activity,newStepFragment)
                                .commit();
                        setStepsActivityTitle();
                        showOrHidePrevNextButtons();
                    }
                }
            });*/

            showOrHidePrevNextButtons();

        } else {
            phonePortraitMode = false;
            Log.d(TAG,"************** LANDSCAPE MODE ACTIVE *****************");
            StepDetailsFragment stepDetailsLandscapeFragment = (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_fragment);
            if (stepDetailsLandscapeFragment != null) {
                stepDetailsLandscapeFragment.setStepsArrayList(recipeStepsArrayList, currentStepIndex);
                stepDetailsLandscapeFragment.updateUI();
            }
        }

    }

    private void setStepsActivityTitle(){
        if(getSupportActionBar() != null){
            if(recipeStepsArrayList != null){
                if(Integer.parseInt(recipeStepsArrayList.get(currentStepIndex).getId())>0){
                    getSupportActionBar().setTitle("Step " + recipeStepsArrayList.get(currentStepIndex).getId());
                }
                else {
                    getSupportActionBar().setTitle("Intro");
                }
            }
        }
    }

    private void showOrHidePrevNextButtons(){
        FloatingActionButton fabPreviousStep = findViewById(R.id.fab_previous_step);
        FloatingActionButton fabNextStep = findViewById(R.id.fab_next_step);
        /*Button buttonPreviousStep = findViewById(R.id.button_previous_step);
        Button buttonNextStep = findViewById(R.id.button_next_step);*/
        if(currentStepIndex == 0){
            //buttonPreviousStep.setVisibility(View.INVISIBLE);
            fabPreviousStep.setEnabled(false);
        } else if(currentStepIndex == (recipeStepsArrayList.size()-1)){
            //buttonNextStep.setVisibility(View.INVISIBLE);
            fabNextStep.setEnabled(false);
        } else {
            //buttonNextStep.setVisibility(View.VISIBLE);
            //buttonPreviousStep.setVisibility(View.VISIBLE);
            fabNextStep.setEnabled(true);
            fabPreviousStep.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(StepDetailsFragment.CURRENT_STEP_INDEX,currentStepIndex);
    }
}
