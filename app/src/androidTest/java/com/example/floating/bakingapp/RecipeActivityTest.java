package com.example.floating.bakingapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import com.example.floating.bakingapp.ui.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.floating.bakingapp.RecyclerViewMethods.withRecyclerView;
import static com.example.floating.bakingapp.RecyclerViewMethods.withToolbarTitle;
import static org.hamcrest.core.Is.is;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola 2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule = new ActivityTestRule<RecipeActivity>(RecipeActivity.class);

    @Test
    public void checkRecyclerView(){

        //Check if the recipe recycler view is displayed
        onView(withId(R.id.recipe_recylcer_view)).check(matches(isDisplayed()));
    }

    @Test
    public void scrollItemPosition(){

        String RECIPE_NAME = "Nutella Pie";
        String RECIPE_SERVINGS = "8 servings";

        //Try scrolling to the last position in the recycler view
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withRecyclerView(R.id.recipe_recylcer_view).atPositionOnView(0, R.id.recipe_name))
                .check(matches(withText(RECIPE_NAME)));
        onView(withRecyclerView(R.id.recipe_recylcer_view).atPositionOnView(1, R.id.number_of_servings))
                .check(matches(withText(RECIPE_SERVINGS)));
    }

    @Test
    public void recyclerItemTest() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recipe_recylcer_view)).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_ingredients_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_steps_rv)).check(matches(isDisplayed()));
    }

    @Test
    public void RecipeListTabletTest() {

        String RECIPE_NAME = "Cheesecake";

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recipe_recylcer_view)).perform(RecyclerViewActions
                .actionOnItemAtPosition(3, click()));

        boolean isTablet = InstrumentationRegistry.getTargetContext().getResources()
                .getBoolean(R.bool.isTablet);
        if (!isTablet)
            onView(withId(R.id.recipe_step_details_container)).check(doesNotExist());
        else
            onView(withId(R.id.recipe_step_details_container)).check(matches(isDisplayed()));

        onView(isAssignableFrom(Toolbar.class)).check(matches(withToolbarTitle(is((CharSequence) RECIPE_NAME))));
    }

}
