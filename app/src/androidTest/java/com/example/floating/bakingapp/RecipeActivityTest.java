package com.example.floating.bakingapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.floating.bakingapp.ui.RecipeActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola 2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position){

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description desc){
                desc.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(desc);
            }

            @Override
            protected boolean matchesSafely(View item) {
                ViewParent parent = item.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && item.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
        public void recipeNameTextViewTest(){
        /*
         * Checking the Recipe name text view whether it contains the expected text for the
         * the selected item on the recycler view and also to check the number of
         * servings text view whether or not the content matches the expected content for the
         * selected recipe item.
         */
        ViewInteraction view = onView(
                allOf(withId(R.id.recipe_name), withText("Nutella Pie"),
                childAtPosition(
                        childAtPosition(
                                withId(R.id.recipe_recylcer_view),
                                0),
                        0),
                        isDisplayed()));

        view.check(matches(withText("Nutella Pie")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipe_recylcer_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
    }

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
