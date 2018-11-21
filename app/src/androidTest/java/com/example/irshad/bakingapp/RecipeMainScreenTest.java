package com.example.irshad.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeMainScreenTest {

    private static final String LABEL_VIEW_INGREDIENTS = "View Ingredients";
    private static final String TEXT_OFFLINE_MESSAGE = "Your device is offline";
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    /*
     * This test first checks whether the device is connected to the Internet or not.
     * If it is connected, then it waits for the recyclerview to load data and
     * performs a click on the first item and then checks whether the
     * appropriate activity is launched or not. If the device is not connected
     * to the Internet, it checks whether the app displays an offline message
     * to the user.
     */
    @Test
    public void clickRecyclerViewItem_OpensRecipeDetailsActivity(){

        if(isConnected(mainActivityTestRule.getActivity())){
            waitForSeconds(5000);
            onView(ViewMatchers.withId(R.id.recycler_view_main_activity)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
            onView(withId(R.id.textview_recipe_details_ingredients_label)).check(matches(withText(LABEL_VIEW_INGREDIENTS)));
        } else {
            onView(withId(R.id.textview_loading_main_activity)).check(matches(withText(TEXT_OFFLINE_MESSAGE)));
        }
    }

    private void waitForSeconds(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    @Test
    public void clickRecyclerViewItem_DisplaysStepsFragmentInDetailsActivity(){
        if(isConnected(mainActivityTestRule.getActivity())){
            waitForSeconds(5000);
            onView(ViewMatchers.withId(R.id.recycler_view_main_activity)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
            onView(withId(R.id.recyclerview_recipe_steps)).check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.textview_loading_main_activity)).check(matches(withText(TEXT_OFFLINE_MESSAGE)));
        }
    }
}
