package com.example.floating.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.floating.bakingapp.ui.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola 2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void checkRecyclerView(){

        //Check if the recipe recycler view is displayed
        onView(withId(R.id.recipe_recylcer_view)).check(matches(isDisplayed()));
    }

    @Test
    public void scrollItemPosition(){
        //Try scrolling to the last position in the recycler view
        onView(withId(R.id.recipe_recylcer_view))
                .perform(RecyclerViewActions.scrollToPosition(0));
    }
}
