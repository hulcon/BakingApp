package com.example.irshad.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.irshad.bakingapp.Fragments.StepDetailsFragment;
import com.example.irshad.bakingapp.Model.Recipe;

public class StepDetailsActivity extends AppCompatActivity {

    private Recipe.Step recipeStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        recipeStep = getIntent().getParcelableExtra(StepDetailsFragment.PARCELABLE_EXTRA_STEP_DETAIL);
        if(getSupportActionBar() != null){
            if(recipeStep != null){
                if(Integer.parseInt(recipeStep.getId())>0){
                    getSupportActionBar().setTitle("Step " + recipeStep.getId());
                }
                else {
                    getSupportActionBar().setTitle("Intro");
                }
            }
        }
        StepDetailsFragment stepDetailsFragment = (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_fragment);
        if (stepDetailsFragment != null) {
            stepDetailsFragment.setStep(recipeStep);
            stepDetailsFragment.updateUI();
        }

    }
}
