package com.example.irshad.bakingapp;

import android.content.Intent;
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

public class StepDetailsActivity extends AppCompatActivity implements StepDetailsFragment.WasVideoPlayingCallback {

    public static final String TAG = StepDetailsActivity.class.getSimpleName();
    private ArrayList<Recipe.Step> recipeStepsArrayList;
    private int currentStepIndex;
    private boolean phonePortraitMode;

    private boolean mWasVideoPlayingWhenRotated;
    private long mVideoPositionWhenRotated;
    private boolean mPlayWhenReadyStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Intent parentIntent = getIntent();
        if(parentIntent != null){
            recipeStepsArrayList = parentIntent.getParcelableArrayListExtra(StepDetailsFragment.PARCELABLE_EXTRA_STEP_ARRAY_LIST);
            mWasVideoPlayingWhenRotated = parentIntent.getBooleanExtra(StepDetailsFragment.EXTRA_KEY_WAS_VIDEO_PLAYING, false);
            mVideoPositionWhenRotated = parentIntent.getLongExtra(StepDetailsFragment.EXTRA_KEY_PLAYER_CURRENT_POSITION, 0);
            mPlayWhenReadyStatus = parentIntent.getBooleanExtra(StepDetailsFragment.EXTRA_KEY_PLAYER_WHEN_READY, true);
        }



        if(savedInstanceState != null){
            currentStepIndex = savedInstanceState.getInt(StepDetailsFragment.CURRENT_STEP_INDEX);
        } else {
            currentStepIndex = getIntent().getIntExtra(StepDetailsFragment.CURRENT_STEP_INDEX, 0);
        }

        setStepsActivityTitle();


        if(findViewById(R.id.constraintlayout_step_detail_phone_portrait_layout) != null){
            phonePortraitMode = true;
            Log.d(TAG,"************** PORTRAIT MODE ACTIVE ***************** iNDEX IS" + currentStepIndex);

            if(savedInstanceState == null) {
                StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_details_fragment_container_for_step_activity, stepDetailsFragment)
                        .commit();
                stepDetailsFragment.setStepsArrayList(recipeStepsArrayList, currentStepIndex);
                if(mWasVideoPlayingWhenRotated){
                    stepDetailsFragment.resumeVideoFromTime(mVideoPositionWhenRotated, mPlayWhenReadyStatus);
                }
            } else {
                StepDetailsFragment stepDetailsPortraitFragment = (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_fragment_container_for_step_activity);
                if (stepDetailsPortraitFragment != null) {
                    stepDetailsPortraitFragment.setStepsArrayList(recipeStepsArrayList, currentStepIndex);
                }
            }


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

            showOrHidePrevNextButtons();

        } else {
            phonePortraitMode = false;
            Log.d(TAG,"************** LANDSCAPE MODE ACTIVE *****************");

            if(savedInstanceState == null) {
                StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_details_fragment_container_for_step_activity, stepDetailsFragment)
                        .commit();
                stepDetailsFragment.setStepsArrayList(recipeStepsArrayList, currentStepIndex);
            } else {
                StepDetailsFragment stepDetailsLandscapeFragment = (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_fragment_container_for_step_activity);
                if (stepDetailsLandscapeFragment != null) {
                    stepDetailsLandscapeFragment.setStepsArrayList(recipeStepsArrayList, currentStepIndex);
                }
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
        if(currentStepIndex == 0){
            fabPreviousStep.setEnabled(false);
        } else if(currentStepIndex == (recipeStepsArrayList.size()-1)){
            fabNextStep.setEnabled(false);
        } else {
            fabNextStep.setEnabled(true);
            fabPreviousStep.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(StepDetailsFragment.CURRENT_STEP_INDEX,currentStepIndex);
    }


    @Override
    public void onDeviceRotatedWhileVideoPlaying(int currentStepIndex, boolean videoPlayingWhenDeviceRotated, long videoCurrentPosition, boolean playWhenReadyStatus) {

    }
}
